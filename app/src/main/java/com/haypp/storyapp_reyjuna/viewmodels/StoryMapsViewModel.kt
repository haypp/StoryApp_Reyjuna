package com.haypp.storyapp_reyjuna.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.haypp.storyapp_reyjuna.data.StoryRepository
import com.haypp.storyapp_reyjuna.data.UserModels

class StoryMapsViewModel(private val repo : StoryRepository) : ViewModel(){
    fun getStoryLocation(token: String) =
        repo.getStoryLoc(token)
}