package com.haypp.storyapp_reyjuna.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.haypp.storyapp_reyjuna.api.ApiConfig
import com.haypp.storyapp_reyjuna.data.AllStoriesResponse
import com.haypp.storyapp_reyjuna.data.UserModels
import com.haypp.storyapp_reyjuna.data.UserPref
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref : UserPref) : ViewModel() {
    private val _allStories = MutableLiveData<AllStoriesResponse>()
    val allStories: LiveData<AllStoriesResponse> = _allStories

    fun getUser(): LiveData<UserModels> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
    fun getAllStories(token: String) {
        val client = ApiConfig.getApiService().allStories(token)
        client.enqueue(object : Callback<AllStoriesResponse> {
            override fun onResponse(
                call: Call<AllStoriesResponse>,
                response: Response<AllStoriesResponse>
            ) {
                if (response.isSuccessful) {
                    _allStories.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<AllStoriesResponse>, t: Throwable) {
                Log.e("MainVM", "OnFailure : ${t.message}")
            }
        })
    }
}