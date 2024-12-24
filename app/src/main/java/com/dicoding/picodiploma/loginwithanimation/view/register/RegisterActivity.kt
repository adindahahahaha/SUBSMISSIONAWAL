package com.dicoding.picodiploma.loginwithanimation.view.register

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityRegisterBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.passwordEditText.addTextChangedListener { text ->
            val password = text.toString()
            if (password.length < 8) {
                binding.passwordEditTextLayout.error = "Password minimal 8 karakter"
            } else {
                binding.passwordEditTextLayout.error = null // Hilangkan error jika valid
            }
        }
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString() // Pastikan ada input untuk name
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (password.length >= 8) {
                binding.passwordEditTextLayout.error = null
                registerViewModel.register(name, email, password)
                    .observe(this, Observer { response ->
                        if (response != null) {
                            if (!response.error!!) {
                                // Show success dialog with message
                                AlertDialog.Builder(this).apply {
                                    setTitle("Yeah!")
                                    setMessage(response.message ?: "User Created") // API response message
                                    setPositiveButton("Lanjut") { _, _ -> finish() } // Close and return to login
                                    create()
                                    show()
                                }
                            } else {
                                // Show error dialog with API error message
                                AlertDialog.Builder(this).apply {
                                    setTitle("Error")
                                    setMessage(response.message ?: "Registrasi gagal. Coba lagi.")
                                    setPositiveButton("OK") { _, _ -> }
                                    create()
                                    show()
                                }
                            }
                        } else {
                            // If response is null, show a generic error
                            AlertDialog.Builder(this).apply {
                                setTitle("Error")
                                setMessage("Terjadi kesalahan. Silakan cek koneksi internet Anda.")
                                setPositiveButton("OK") { _, _ -> }
                                create()
                                show()
                            }
                        }
                    })} else {
                binding.passwordEditTextLayout.error = "Password minimal 8 karakter"
            }
        }
    }
}