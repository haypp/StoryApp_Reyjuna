package com.haypp.storyapp_reyjuna.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haypp.storyapp_reyjuna.data.LoginRequest
import com.haypp.storyapp_reyjuna.data.StoryRepository
import com.haypp.storyapp_reyjuna.data.UserModels
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: StoryRepository) : ViewModel() {
    fun meLogin(loginReq : LoginRequest) = repo.rLogin(loginReq)
    fun saveUser(user: UserModels) {
        viewModelScope.launch {
            repo.saveUserData(user)
        }
    }
    fun logout() {
        viewModelScope.launch {
            repo.logout()
        }
    }
}