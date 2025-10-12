package com.jeyix.schooljeyix.ui.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.ui.admin.AdminMainActivity
import com.jeyix.schooljeyix.ui.login.LoginActivity
import com.jeyix.schooljeyix.ui.parent.ParentMainActivity
import com.jeyix.schooljeyix.ui.student.StudentMainActivity
import com.jeyix.schooljeyix.ui.teacher.TeacherMainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashScreen.setKeepOnScreenCondition {
            viewModel.authState.value is SplashAuthState.Loading
        }

        observeAuthState()
    }

    private fun observeAuthState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authState.collect { state ->
                    val destination: Class<*>? = when (state) {
                        is SplashAuthState.Authenticated -> {
                            when (state.userRole) {
                                "ROLE_PARENT" -> ParentMainActivity::class.java
                                "ROLE_ADMIN" -> AdminMainActivity::class.java
                                "ROLE_TEACHER" -> TeacherMainActivity::class.java
                                "ROLE_STUDENT" -> StudentMainActivity::class.java
                                else -> LoginActivity::class.java
                            }
                        }
                        is SplashAuthState.Unauthenticated -> LoginActivity::class.java
                        is SplashAuthState.Loading -> null
                    }

                    destination?.let {
                        startExitAnimation(it)
                    }
                }
            }
        }
    }

    private fun startExitAnimation(destination: Class<*>) {
        val logo = findViewById<ImageView>(R.id.imgLogo)
        val appName = findViewById<TextView>(R.id.tvAppName)

        val fadeOut = ObjectAnimator.ofFloat(findViewById<View>(android.R.id.content), "alpha", 1f, 0f).apply {
        }

        val logoAnimator = ObjectAnimator.ofPropertyValuesHolder(logo,
            PropertyValuesHolder.ofFloat("alpha", 0f, 1f),
            PropertyValuesHolder.ofFloat("translationY", 100f, 0f)
        ).apply {
            interpolator = OvershootInterpolator()
            duration = 800L
            startDelay = 200L
        }

        val appNameAnimator = ObjectAnimator.ofFloat(appName, "alpha", 0f, 1f).apply {
            duration = 800L
            startDelay = 200L
        }

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(logoAnimator, appNameAnimator)

        animatorSet.doOnEnd {
            startActivity(Intent(this, destination))
            finish()
        }

        animatorSet.start()
    }
}