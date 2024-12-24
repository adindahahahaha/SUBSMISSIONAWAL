package com.dicoding.picodiploma.loginwithanimation.di

import android.content.Context
import com.dicoding.picodiploma.loginwithanimation.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUser().first() }  // Ambil user secara synchronous
        val apiService = ApiConfig.getApiService(user.token) // Menyediakan ApiService dengan token
        return UserRepository.getInstance(apiService, pref)
    }

    // Menyediakan StoryRepository dengan token user yang ada
    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUser().first() }  // Ambil user secara synchronous
        val apiService = ApiConfig.getApiService(user.token) // Menggunakan token dari user
        return StoryRepository.getInstance(apiService, pref)
    }
}
