package com.dicoding.picodiploma.loginwithanimation.data

import com.dicoding.picodiploma.loginwithanimation.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    // Mengambil list story dari API
    suspend fun getStories(token: String) = apiService.getStories(token)

    companion object {
        @Volatile
        private var INSTANCE: StoryRepository? = null

        fun getInstance(apiService: ApiService, userPreference: UserPreference): StoryRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: StoryRepository(apiService, userPreference).also { INSTANCE = it }
            }
        }
    }
}
