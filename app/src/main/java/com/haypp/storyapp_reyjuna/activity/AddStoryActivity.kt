package com.haypp.storyapp_reyjuna.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.haypp.storyapp_reyjuna.R
import com.haypp.storyapp_reyjuna.data.ViewModelFactory
import com.haypp.storyapp_reyjuna.databinding.ActivityAddStoryBinding
import com.haypp.storyapp_reyjuna.etc.Result
import com.haypp.storyapp_reyjuna.etc.createTempFile
import com.haypp.storyapp_reyjuna.etc.reduceImageSize
import com.haypp.storyapp_reyjuna.etc.uriToFile
import com.haypp.storyapp_reyjuna.viewmodels.AddStoryViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var currentPhotoPath: String
    private lateinit var factory: ViewModelFactory
    private lateinit var addViewModel: AddStoryViewModel
    private lateinit var fusedLoc: FusedLocationProviderClient
    private var getFile: File? = null
    private var lat: Double? = null
    private var lon: Double? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_story)
        supportActionBar?.hide()
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)
        addViewModel = ViewModelProvider(this, factory)[AddStoryViewModel::class.java]

        if (!cekPremissionOke()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        binding.btncamera.setOnClickListener { ambilphoto() }
        binding.btngaleri.setOnClickListener { bukagaleri() }
        binding.buttonAdd.setOnClickListener { uploadStory() }
        getMyLocation()
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLoc = LocationServices.getFusedLocationProviderClient(this)
            fusedLoc.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude
                    lon = location.longitude
                    Toast.makeText(
                        this,
                        "Saved Location",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Tidak ada lokasi",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            (Manifest.permission.ACCESS_COARSE_LOCATION)

        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun bukagaleri() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun ambilphoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.haypp.storyapp_reyjuna",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!cekPremissionOke()) {
                Toast.makeText(this, "Izinkan Kamera", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun cekPremissionOke() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.previewImage.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            binding.previewImage.setImageURI(selectedImg)
        }
    }

    private fun uploadStory() {
        if (getFile != null) {
            addViewModel.getUser().observe(this) { it ->
                val token = "Bearer ${it.token}"
                val file = reduceImageSize(getFile as File)
                val desc =
                    "${binding.edAddDescription.text}".toRequestBody("text/plain".toMediaTypeOrNull())
                val reqImgFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo", file.name, reqImgFile
                )
                addViewModel.getUser().observe(this) {
                    addViewModel.addStory(token, imageMultipart, desc, lat, lon)
                        .observe(this@AddStoryActivity) {
                            when (it) {
                                is Result.Success -> {
                                    startActivity(Intent(this, MainActivity::class.java))
                                    Toast.makeText(this, "Upload Succes", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                is Result.Loading -> {
                                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                                }
                                is Result.Error -> {
                                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                                    Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                }
            }
        } else {
            Toast.makeText(this, "Pilih Gambar", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}