package com.haypp.storyapp_reyjuna.viewmodels

import androidx.lifecycle.ViewModel
import com.haypp.storyapp_reyjuna.data.StoryRepository

class RegisterViewModel(private val repo: StoryRepository) : ViewModel() {
    fun doRegister(name: String, email: String, password: String) =
        repo.rRegister(name, email, password)
}