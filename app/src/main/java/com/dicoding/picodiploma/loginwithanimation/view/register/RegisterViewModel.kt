package com.dicoding.picodiploma.loginwithanimation.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dicoding.picodiploma.loginwithanimation.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import retrofit2.HttpException
import java.io.IOException

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun register(name: String, email: String, password: String): LiveData<RegisterResponse?> = liveData {
        try {
            val response = userRepository.register(name, email, password) // Panggil UserRepository
            emit(response)
        }catch (e: HttpException) {
            emit(RegisterResponse(error = true, message = e.response()?.errorBody()?.string()))
        }
    }
}

