package com.haypp.storyapp_reyjuna.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.haypp.storyapp_reyjuna.viewmodels.LoginViewModel
import com.haypp.storyapp_reyjuna.viewmodels.MainViewModel
import com.haypp.storyapp_reyjuna.viewmodels.RegisterViewModel

class ViewModelFactory(private val pref: UserPref) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(pref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}