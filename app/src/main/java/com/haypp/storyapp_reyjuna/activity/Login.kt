package com.haypp.storyapp_reyjuna.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.haypp.storyapp_reyjuna.R
import com.haypp.storyapp_reyjuna.data.*
import com.haypp.storyapp_reyjuna.databinding.ActivityLoginBinding
import com.haypp.storyapp_reyjuna.etc.Result
import com.haypp.storyapp_reyjuna.viewmodels.LoginViewModel

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var lviewmodel: LoginViewModel
    private lateinit var factory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setButton()
        setupAnimation()
        showLoading(false)
        
        factory = ViewModelFactory.getInstance(this)
        lviewmodel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        
        binding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) =
                setButton()
            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.btnLoginOne.setOnClickListener {
            buttonClicked()
        }
        binding.btnRegister.setOnClickListener {
            pindahregister()
        }
    }

    private fun pindahregister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun buttonClicked() {
        val resp = LoginRequest(binding.edRegisterEmail.text.toString(), binding.edRegisterPassword.text.toString())
        showLoading(true)
        lviewmodel.meLogin(resp).observe(this) {
            when (it) {
                is Result.Success -> {
                    showLoading(false)
                    val response = it.data
                    saveUserData(UserModels(
                            response.loginResult?.name.toString(),
                            response.loginResult?.token.toString(),
                            true))
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is Result.Loading -> showLoading(true)
                is Result.Error -> {
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
                else -> {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        }
    }

    fun setButton() {
        val result = binding.edRegisterPassword.text.toString()
        binding.btnLoginOne.isEnabled = !(result.length < 6 || result.length > 20)
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun saveUserData(user: UserModels) {
        lviewmodel.saveUser(user)
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.tvHero, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val login = ObjectAnimator.ofFloat(binding.btnLoginOne, View.ALPHA, 1f).setDuration(300)
        val email = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(300)
        val emailLayout =
            ObjectAnimator.ofFloat(binding.emailTextInputLayout, View.ALPHA, 1f).setDuration(300)
        val password =
            ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(300)
        val passwordLayout =
            ObjectAnimator.ofFloat(binding.passwordTextInputLayout, View.ALPHA, 1f).setDuration(300)
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(email, emailLayout, password, passwordLayout, login,register)
            start()
        }
    }
}
