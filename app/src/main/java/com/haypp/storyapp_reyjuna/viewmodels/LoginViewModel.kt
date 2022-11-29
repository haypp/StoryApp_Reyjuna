package com.haypp.storyapp_reyjuna.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haypp.storyapp_reyjuna.api.ApiConfig
import com.haypp.storyapp_reyjuna.data.LoginRequest
import com.haypp.storyapp_reyjuna.data.LoginResponse
import com.haypp.storyapp_reyjuna.data.UserPref
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPref) : ViewModel() {
    private val vloginUser = MutableLiveData<LoginResponse>()
    val loginUser: LiveData<LoginResponse> = vloginUser

    fun meLogin(loginReq : LoginRequest) {
        val client = ApiConfig.getApiService().loginDO(loginReq)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error!!) {
                        vloginUser.postValue(response.body())
                    }
                } else {
                    vloginUser.value = LoginResponse(null, true, "Email or Password is incorrect")
                }
        }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("LoginViewModel", "onFailure: ${t.message.toString()}")
            }
        })
    }
    fun login() {
        viewModelScope.launch {
            pref.login()
        }
    }
}