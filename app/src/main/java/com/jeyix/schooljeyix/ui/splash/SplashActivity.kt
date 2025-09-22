package com.jeyix.schooljeyix.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.imgLogo)

        logo.alpha = 0f
        logo.scaleX = 0.3f
        logo.scaleY = 0.3f
        logo.rotation = -90f

        logo.animate()
            .alpha(1f)
            .rotation(0f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(1200)
            .setInterpolator(OvershootInterpolator())
            .withEndAction {
                logo.animate()
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .setDuration(200)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .withEndAction {
                        logo.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(200)
                            .withEndAction {
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                            .start()
                    }
                    .start()
            }
            .start()
    }
}