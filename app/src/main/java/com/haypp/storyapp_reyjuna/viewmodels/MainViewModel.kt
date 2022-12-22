package com.haypp.storyapp_reyjuna.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.haypp.storyapp_reyjuna.data.ListStory
import com.haypp.storyapp_reyjuna.data.StoryRepository
import com.haypp.storyapp_reyjuna.data.UserModels

class MainViewModel(private val repo : StoryRepository) : ViewModel() {
    fun getStory() : LiveData<PagingData<ListStory>> {
        return repo.getStory().cachedIn(viewModelScope) }
}

