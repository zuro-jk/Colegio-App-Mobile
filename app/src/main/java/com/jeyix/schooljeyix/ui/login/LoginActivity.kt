package com.jeyix.schooljeyix.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.button.MaterialButton
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.data.local.datastore.UserPreferences
import com.jeyix.schooljeyix.domain.usecase.AuthRepository
import com.jeyix.schooljeyix.ui.parent.ParentMainActivity
import com.jeyix.schooljeyix.ui.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var userPrefernces: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilPassword)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        val tvRegister = findViewById<android.widget.TextView>(R.id.tvRegister)

        btnLogin.setOnClickListener { attemptLogin() }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun attemptLogin() {
        val usernameOrEmail = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        var valid = true

        if (usernameOrEmail.isEmpty()) {
            tilEmail.error = "Ingrese su usuario o correo electrónico"
            valid = false
        } else {
            tilEmail.error = null
        }

        if (password.isEmpty()) {
            tilPassword.error = "Ingrese su contraseña"
            valid = false
        } else {
            tilPassword.error = null
        }

        if (!valid) return

        lifecycleScope.launch {
            try {
                Log.d("LoginActivity", "🔹 Intentando login con: $usernameOrEmail")

                val response = authRepository.login(usernameOrEmail, password)

                Log.d("LoginActivity", "🔹 Respuesta del servidor: $response")

                if (response.success) {
                    val accessToken = response.data?.accessToken
                    val sessionId = response.data?.sessionId
                    val user = response.data?.user

                    userPrefernces.saveUserData(accessToken, sessionId, user)


                    Toast.makeText(this@LoginActivity, "Login exitoso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, ParentMainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        response.message ?: "Credenciales inválidas",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("LoginActivity", "❌ Error al iniciar sesión", e)
                Toast.makeText(
                    this@LoginActivity,
                    "Error de conexión: ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
