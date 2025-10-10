package com.jeyix.schooljeyix.ui.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnticipateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashScreen.setOnExitAnimationListener { splashScreenView ->

            val logo = findViewById<ImageView>(R.id.imgLogo)
            val appName = findViewById<TextView>(R.id.tvAppName)

            // --- ANIMACIÓN PARA EL SPLASH DEL SISTEMA (DESAPARECER) ---
            // CAMBIO CLAVE: Apuntamos a splashScreenView.view en lugar de solo splashScreenView
            val fadeOut = ObjectAnimator.ofFloat(splashScreenView.view, "alpha", 1f, 0f).apply {
                interpolator = AccelerateDecelerateInterpolator()
                duration = 400L
                doOnEnd { splashScreenView.remove() }
            }

            // --- ANIMACIÓN PARA NUESTRO LOGO (APARECER Y MOVERSE) ---
            val logoAlpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f)
            val logoTranslationY = PropertyValuesHolder.ofFloat("translationY", 100f, 0f)

            val logoAnimator = ObjectAnimator.ofPropertyValuesHolder(logo, logoAlpha, logoTranslationY).apply {
                interpolator = OvershootInterpolator()
                duration = 800L
                startDelay = 200L
            }

            // --- ANIMACIÓN PARA EL NOMBRE DE LA APP (APARECER) ---
            val appNameAnimator = ObjectAnimator.ofFloat(appName, "alpha", 0f, 1f).apply {
                duration = 800L
                startDelay = 200L
            }

            // --- JUNTAMOS TODAS LAS ANIMACIONES ---
            val animatorSet = AnimatorSet()
            animatorSet.playTogether(fadeOut, logoAnimator, appNameAnimator)

            // Cuando TODO termine, navegamos a la siguiente pantalla
            animatorSet.doOnEnd {
                navigateToLogin()
            }

            // ¡Iniciamos la animación!
            animatorSet.start()
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}