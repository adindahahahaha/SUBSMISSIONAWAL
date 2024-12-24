package com.dicoding.picodiploma.loginwithanimation.data

import com.dicoding.picodiploma.loginwithanimation.ApiService
import com.dicoding.picodiploma.loginwithanimation.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class UserRepository private constructor(
    private val apiService: ApiService,  // Tambahkan ApiService
    private val userPreference: UserPreference
) {

    // Fungsi untuk login
    suspend fun login(email: String, password: String): LoginResponse? {
        return withContext(Dispatchers.IO) {
            val response = apiService.login(email, password)
            if (response?.error == false) {
                val user = UserModel(email = email, token = response.loginResult?.token.orEmpty(), isLogin = true)
                userPreference.saveSession(user)
            }
            return@withContext response
        }
    }

    // Fungsi untuk register
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return withContext(Dispatchers.IO) {
            apiService.register(name, email, password)
        }
    }

    // Fungsi untuk menyimpan sesi login
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    // Fungsi untuk mengambil sesi user
    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    // Fungsi untuk logout
    suspend fun logout() {
        userPreference.logout()
    }
    suspend fun getToken(): String {
        val user = userPreference.getUser().first()  // Ambil user secara synchronous
        return user.token  // Kembalikan token
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}
