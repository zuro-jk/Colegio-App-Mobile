package com.jeyix.schooljeyix.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.button.MaterialButton
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.data.repository.UserRepositoryImpl
import com.jeyix.schooljeyix.domain.usecase.UserUseCases
import com.jeyix.schooljeyix.ui.parent.ParentMainActivity
import com.jeyix.schooljeyix.ui.register.RegisterActivity
import com.jeyix.schooljeyix.ui.student.StudentMainActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton

    private val useCases = UserUseCases(UserRepositoryImpl())

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
        val password = etPassword.text.toString()

        var valid = true

        if (usernameOrEmail.isEmpty()) {
            tilEmail.error = "El correo o usuario es obligatorio"
            valid = false
        } else {
            tilEmail.error = null
        }

        if (password.isEmpty()) {
            tilPassword.error = "La contraseña es obligatoria"
            valid = false
        } else {
            tilPassword.error = null
        }


        if (!valid) return

        lifecycleScope.launch {
            try {
                val success= useCases.login(usernameOrEmail, password)
                if (success) {
                    Toast.makeText(this@LoginActivity, "Login exitoso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, ParentMainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@LoginActivity, "Error de conexión: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
