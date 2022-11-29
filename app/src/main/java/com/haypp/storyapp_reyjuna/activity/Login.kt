package com.haypp.storyapp_reyjuna.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.haypp.storyapp_reyjuna.R
import com.haypp.storyapp_reyjuna.data.LoginRequest
import com.haypp.storyapp_reyjuna.data.UserPref
import com.haypp.storyapp_reyjuna.data.ViewModelFactory
import com.haypp.storyapp_reyjuna.databinding.ActivityLoginBinding
import com.haypp.storyapp_reyjuna.viewmodels.LoginViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var lviewmodel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setButton()
        setupAnimation()
        showLoading(false)
        lviewmodel = ViewModelProvider(this, ViewModelFactory(UserPref.getInstance(dataStore))
        )[LoginViewModel::class.java]
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
        lviewmodel.meLogin(resp)
        lviewmodel.login()
        showLoading(true)
        lviewmodel.loginUser.observe(this) {
            val e = it.error?.toString()
            Log.e("error apa ini : ", e.toString())
            if (it.error?.toString().equals("false")) {
                showLoading(false)
                val loginSession = LoginSession(this)
                loginSession.saveAuthToken(it.loginResult?.token.toString())
                Log.d(
                    "LoginActivity",
                    "token : ${loginSession.passToken().toString()}"
                )
                val intent = Intent(this, MainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
                } else {
                showLoading(false)
                binding.edRegisterEmail.text = null
                binding.edRegisterPassword.text = null
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
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
