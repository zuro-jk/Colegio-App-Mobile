package com.jeyix.schooljeyix.ui.parent

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jeyix.schooljeyix.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParentMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.parent_nav_host_fragment) as? NavHostFragment
        val navController = navHostFragment?.navController
            ?: throw IllegalStateException("NavHostFragment no encontrado")

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_parent_dashboard,
                R.id.nav_parent_students,
                R.id.nav_parent_payments
            )
        )

        bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (appBarConfiguration.topLevelDestinations.contains(destination.id)) {
                topAppBar.navigationIcon = null
            } else {
                topAppBar.setNavigationIcon(R.drawable.ic_arrow_back_24)
                topAppBar.setNavigationOnClickListener {
                    navController.navigateUp()
                }
            }
        }

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_notifications -> {
                    navController.navigate(R.id.nav_parent_notifications)
                    true
                }
                R.id.action_profile -> {
                    navController.navigate(R.id.nav_parent_profile)
                    true
                }
                else -> false
            }
        }

    }
}