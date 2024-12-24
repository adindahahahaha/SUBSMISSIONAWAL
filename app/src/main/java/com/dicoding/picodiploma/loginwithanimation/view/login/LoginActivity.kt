package com.dicoding.picodiploma.loginwithanimation.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (password.length >= 8) {
                // Reset error jika password valid
                binding.passwordEditTextLayout.error = null
                viewModel.login(email, password) { loginResponse ->
                    if (loginResponse != null && loginResponse.error == false) {
                        // Simpan session jika login berhasil
                        val token = loginResponse.loginResult?.token.orEmpty()
                        viewModel.saveSession(UserModel(email, token))

                        // Tampilkan dialog login berhasil
                        AlertDialog.Builder(this).apply {
                            setTitle("Yeah!")
                            setMessage("Anda berhasil login. Sudah tidak sabar untuk berbagi cerita ya?")
                            setPositiveButton("Lanjut") { _, _ ->
                                val intent = Intent(context, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    } else {
                        // Tampilkan error jika login gagal
                        AlertDialog.Builder(this).apply {
                            setTitle("Login Gagal")
                            setMessage("Email atau password yang Anda masukkan salah.")
                            setPositiveButton("Coba Lagi", null)
                            create()
                            show()
                        }
                    }
                }
            } else {
                // Tampilkan error jika password kurang dari 8 karakter
                binding.passwordEditTextLayout.error = "Password minimal 8 karakter"
            }
        }
    }
}