package com.haypp.storyapp_reyjuna.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.haypp.storyapp_reyjuna.data.StoryRepository
import com.haypp.storyapp_reyjuna.api.ApiConfig
import com.haypp.storyapp_reyjuna.data.UserPref

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
object Injection {

    fun provideRepository(context: Context): StoryRepository {
        val preferences = UserPref.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(preferences, apiService)
    }
}