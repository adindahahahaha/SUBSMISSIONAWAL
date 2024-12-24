package com.dicoding.picodiploma.loginwithanimation.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    // Fungsi untuk melakukan login dengan API
    fun login(email: String, password: String, onResult: (LoginResponse?) -> Unit) {
        viewModelScope.launch {
            val response = repository.login(email, password)  // Panggil fungsi login di repository
            onResult(response)  // Mengembalikan hasil login ke UI
        }
    }

    // Fungsi untuk menyimpan session setelah login
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}
