package com.jeyix.schooljeyix.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.data.remote.feature.auth.request.RegisterRequest
import com.jeyix.schooljeyix.databinding.ActivityRegisterBinding
import com.jeyix.schooljeyix.domain.model.User
import com.jeyix.schooljeyix.domain.usecase.auth.AuthRepository
import com.jeyix.schooljeyix.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListener()
        observeRegisterState()
    }

    private fun setupClickListener() {
        binding.btnRegister.setOnClickListener {

            val registerRequest = RegisterRequest(
                firstName = binding.etFirstName.text.toString().trim(),
                lastName = binding.etLastName.text.toString().trim(),
                username = binding.etUsername.text.toString().trim(),
                email = binding.etEmail.text.toString().trim(),
                password = binding.etPassword.text.toString().trim(),
            )

            viewModel.onRegisterClicked(
                registerRequest,
                confirmPassword = binding.etConfirmPassword.text.toString().trim()
            )
        }

        binding.tvGoLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun observeRegisterState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerState.collect { state ->
                    when (state) {
                        is RegisterState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.btnRegister.isEnabled = false
                        }
                        is RegisterState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@RegisterActivity, state.message, Toast.LENGTH_LONG).show()
                            finish()
                        }
                        is RegisterState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.btnRegister.isEnabled = true
                            Toast.makeText(this@RegisterActivity, state.message, Toast.LENGTH_LONG).show()
                        }
                        is RegisterState.Idle -> {
                            binding.progressBar.visibility = View.GONE
                            binding.btnRegister.isEnabled = true
                        }
                    }
                }
            }
        }
    }

}