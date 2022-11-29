package com.haypp.storyapp_reyjuna.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.haypp.storyapp_reyjuna.R
import com.haypp.storyapp_reyjuna.data.UserModels
import com.haypp.storyapp_reyjuna.data.UserPref
import com.haypp.storyapp_reyjuna.data.ViewModelFactory
import com.haypp.storyapp_reyjuna.databinding.ActivityRegisterBinding
import com.haypp.storyapp_reyjuna.viewmodels.RegisterViewModel
import androidx.core.widget.addTextChangedListener as adt

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var regisVM: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        regisVM = ViewModelProvider(this, ViewModelFactory(UserPref.getInstance(dataStore))
        )[RegisterViewModel::class.java]
        showLoading(false)
        buttonEnabler()
        setbuttoon()
        setupAnimation()
        binding.btnRegister.setOnClickListener{
            sendDataUser()
        }
    }

    private fun sendDataUser() {
        showLoading(true)
        val name = binding.edRegisterName.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()
        regisVM.sendUser(UserModels(name, email, password,false))
        regisVM.doRegister(name, email, password)
        regisVM.registerUser.observe(this) {
            if (!it.error) {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                showLoading(false)
                finish()
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                showLoading(false)
                binding.edRegisterEmail.text = null
                binding.edRegisterPassword.text = null
            }
        }
    }
    private fun setbuttoon() {
        binding.apply {
            edRegisterName.adt{ buttonEnabler()}
            edRegisterEmail.adt{ buttonEnabler() }
            edRegisterPassword.adt{ buttonEnabler() }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun buttonEnabler() {
        val vname = binding.edRegisterName.text
        val vemail = binding.edRegisterEmail.text
        val vpassword = binding.edRegisterPassword.text
        binding.btnRegister.isEnabled = !(vname.isNullOrEmpty() && vemail.isNullOrEmpty() && vpassword.isNullOrEmpty())
    }
    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.tvHero, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(300)
        val email = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(300)
        val emailLayout =
            ObjectAnimator.ofFloat(binding.emailTextInputLayout, View.ALPHA, 1f).setDuration(300)
        val password =
            ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(300)
        val passwordLayout =
            ObjectAnimator.ofFloat(binding.passwordTextInputLayout, View.ALPHA, 1f).setDuration(300)
        val name = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(300)
        val namelayout = ObjectAnimator.ofFloat(binding.nameTextInputLayout, View.ALPHA, 1f)
            .setDuration(300)

        AnimatorSet().apply {
            playSequentially(name, namelayout,email, emailLayout, password, passwordLayout, register)
            start()
        }
    }
}