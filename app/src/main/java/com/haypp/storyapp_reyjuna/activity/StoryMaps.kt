package com.haypp.storyapp_reyjuna.activity

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.haypp.storyapp_reyjuna.R
import com.haypp.storyapp_reyjuna.data.ListStory
import com.haypp.storyapp_reyjuna.data.ViewModelFactory
import com.haypp.storyapp_reyjuna.databinding.ActivityStoryMapsBinding
import com.haypp.storyapp_reyjuna.etc.Result
import com.haypp.storyapp_reyjuna.viewmodels.AddStoryViewModel
import com.haypp.storyapp_reyjuna.viewmodels.StoryMapsViewModel

class StoryMaps : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding : ActivityStoryMapsBinding
    private lateinit var gMap : GoogleMap
    private lateinit var mapViewModel : StoryMapsViewModel
    private lateinit var adViewModel : AddStoryViewModel
    private lateinit var factory : ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        factory = ViewModelFactory.getInstance(this)
        mapViewModel = ViewModelProvider(this, factory)[StoryMapsViewModel::class.java]
        adViewModel = ViewModelProvider(this, factory)[AddStoryViewModel::class.java]
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setDatamaps()
    }

    private fun setDatamaps() {
        adViewModel.getUser().observe(this){ it ->
            val token = "Bearer ${it.token}"
            mapViewModel.getStoryLocation(token).observe(this){
                when(it){
                    is Result.Loading -> {}
                    is Result.Success -> showMarker(it.data.ListStory)
                    is Result.Error -> Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showMarker(listStory: List<ListStory>) {
        for (story in listStory) {
            val latlng = LatLng(story.latitude, story.longitude)
            gMap.addMarker(
                MarkerOptions()
                    .position(latlng)
                    .snippet(story.description + " | " + story.createdAt.toString().removeRange(16,story.createdAt.toString().length))
                    .title(story.name)
            )
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        gMap.uiSettings.isZoomControlsEnabled = true
        gMap.uiSettings.isIndoorLevelPickerEnabled = true
        gMap.uiSettings.isCompassEnabled = true
        gMap.uiSettings.isMapToolbarEnabled = true
        getMyLoc()
        setMapStyle()
    }

    private fun setMapStyle() {
        try {
            val success =
                gMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }



    private fun getMyLoc() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            gMap.isMyLocationEnabled = true
        } else {
            reqPermissionLoc.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    private val reqPermissionLoc =
        registerForActivityResult(ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) { getMyLoc() }
        }

    companion object {
        private const val TAG = "StoryMapsActivity"
    }
}