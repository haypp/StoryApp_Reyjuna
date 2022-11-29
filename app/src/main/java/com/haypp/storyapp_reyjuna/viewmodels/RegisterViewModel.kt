package com.haypp.storyapp_reyjuna.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haypp.storyapp_reyjuna.api.ApiConfig
import com.haypp.storyapp_reyjuna.data.RegisterResponse
import com.haypp.storyapp_reyjuna.data.UserModels
import com.haypp.storyapp_reyjuna.data.UserPref
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val pref: UserPref) : ViewModel() {
    private val vRegister = MutableLiveData<RegisterResponse>()
    val registerUser: LiveData<RegisterResponse> = vRegister

    fun doRegister(username : String, email : String, password : String) {
        val client = ApiConfig.getApiService().registerDO(username, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        vRegister.postValue(response.body())
                    }
                } else {
                    vRegister.value = RegisterResponse(true, "Email already registered")
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.d("RegisterViewModel", "onFailure: ${t.message.toString()}")
            }
        })
    }
    fun sendUser(user: UserModels) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }
}