package com.jeyix.schooljeyix.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.domain.model.User
import com.jeyix.schooljeyix.domain.usecase.AuthRepository
import com.jeyix.schooljeyix.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvGoLogin: TextView

    @Inject
    lateinit var authRepository: AuthRepository

    private val TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        Log.d(TAG, "onCreate: Iniciando vista de registro...")

        etUsername = findViewById(R.id.etUsername)
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvGoLogin = findViewById(R.id.tvGoLogin)

        Log.d(TAG, "onCreate: Componentes inicializados correctamente")

        btnRegister.setOnClickListener { attemptRegister() }

        tvGoLogin.setOnClickListener {
            Log.d(TAG, "onCreate: Click en 'Ir al login'")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun attemptRegister() {
        Log.d(TAG, "attemptRegister: Iniciando validaciones...")

        val username = etUsername.text.toString().trim()
        val firstName = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        Log.d(TAG, "Datos ingresados -> Nombre: $firstName | Apellido: $lastName | Email: $email")

        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Log.w(TAG, "Campos vacíos detectados")
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Log.w(TAG, "Correo electrónico inválido: $email")
            Toast.makeText(this, "Correo electrónico inválido", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Log.w(TAG, "Las contraseñas no coinciden")
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        val user = User(
            firstName = firstName,
            lastName = lastName,
            username = username,
            email = email,
            password = password
        )

        Log.d(TAG, "Usuario preparado para registro: $user")

        lifecycleScope.launch {
            try {
                Log.d(TAG, "Llamando a authRepository.register()...")
                val response = authRepository.register(user)
                Log.d(TAG, "Respuesta recibida -> success=${response.success}, message=${response.message}")

                if (response.success) {
                    Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    Log.i(TAG, "Registro exitoso. Finalizando Activity.")
                    finish()
                } else {
                    Toast.makeText(this@RegisterActivity, response.message, Toast.LENGTH_SHORT).show()
                    Log.w(TAG, "Error del servidor o validación: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al registrar usuario", e)
                Toast.makeText(
                    this@RegisterActivity,
                    "Error de conexión: ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}