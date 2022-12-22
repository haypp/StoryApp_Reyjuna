package com.haypp.storyapp_reyjuna.activity

import android.content.Context
import com.haypp.storyapp_reyjuna.R

class LoginSession(context: Context) {

    private val loginSession =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    fun logoutSession() {
        loginSession.edit().clear().apply()
    }
}