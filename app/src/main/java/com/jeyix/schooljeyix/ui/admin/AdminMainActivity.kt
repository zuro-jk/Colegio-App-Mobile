package com.jeyix.schooljeyix.ui.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.databinding.ActivityAdminMainBinding
import com.jeyix.schooljeyix.domain.usecase.auth.LogoutUseCase
import com.jeyix.schooljeyix.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AdminMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminMainBinding

    @Inject
    lateinit var logoutUseCase: LogoutUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.admin_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            val handled = when (menuItem.itemId) {
                R.id.nav_logout -> {
                    lifecycleScope.launch {
                        logoutUseCase()
                        navigateToLogin()
                    }
                    true
                }
                else -> {
                    menuItem.onNavDestinationSelected(navController)
                }
            }

            binding.drawerLayout.closeDrawer(GravityCompat.START)
            handled
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_profile_shortcut -> {
                    navController.navigate(R.id.nav_profile)
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}