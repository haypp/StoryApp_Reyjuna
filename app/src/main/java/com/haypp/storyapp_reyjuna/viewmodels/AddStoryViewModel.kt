package com.haypp.storyapp_reyjuna.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.haypp.storyapp_reyjuna.data.StoryRepository
import com.haypp.storyapp_reyjuna.data.UserModels
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val storyRepository: StoryRepository) : ViewModel(){
    fun addStory(token: String, file: MultipartBody.Part, description: RequestBody, lat: Double?,
                 lon: Double?) =
        storyRepository.addStory(token, file, description, lat, lon)

    fun getUser(): LiveData<UserModels> {
        return storyRepository.getUserData()
    }
}