package com.jeyix.schooljeyix.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jeyix.schooljeyix.data.remote.feature.auth.request.LoginRequest
import com.jeyix.schooljeyix.databinding.ActivityLoginBinding
import com.jeyix.schooljeyix.ui.admin.AdminMainActivity
import com.jeyix.schooljeyix.ui.parent.ParentMainActivity
import com.jeyix.schooljeyix.ui.register.RegisterActivity
import com.jeyix.schooljeyix.ui.student.StudentMainActivity
import com.jeyix.schooljeyix.ui.teacher.TeacherMainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        observeLoginState()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val request = LoginRequest(
                usernameOrEmail = username,
                password
            )
            viewModel.login(request)
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun observeLoginState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginState.collect { state ->
                    when (state) {
                        is LoginState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.btnLogin.isEnabled = false
                        }
                        is LoginState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@LoginActivity, "Login exitoso", Toast.LENGTH_SHORT).show()

                            when (state.userRole) {
                                "ROLE_PARENT" -> navigateToAndFinish(ParentMainActivity::class.java)
                                "ROLE_ADMIN" -> navigateToAndFinish(AdminMainActivity::class.java)
                                "ROLE_TEACHER" -> navigateToAndFinish(TeacherMainActivity::class.java)
                                 "ROLE_STUDENT" -> navigateToAndFinish(StudentMainActivity::class.java)
                                else -> {
                                    Toast.makeText(this@LoginActivity, "Rol de usuario no reconocido.", Toast.LENGTH_LONG).show()
                                    binding.btnLogin.isEnabled = true
                                }
                            }
                        }
                        is LoginState.Error -> {
                        }
                        is LoginState.Idle -> {
                        }
                    }
                }
            }
        }
    }

    private fun <T : AppCompatActivity> navigateToAndFinish(activityClass: Class<T>) {
        startActivity(Intent(this, activityClass))
        finish()
    }
}
