package com.jeyix.schooljeyix.ui.student

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jeyix.schooljeyix.R

class StudentMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.student_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard,
                R.id.nav_courses,
                R.id.nav_payments
            )
        )

        bottomNav?.setupWithNavController(navController)

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
                    navController.navigate(
                        R.id.nav_notifications,
                        null,
                        androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(R.id.student_nav_graph, false)
                            .build()
                    )
                    true
                }
                R.id.action_profile -> {
                    navController.navigate(
                        R.id.nav_profile,
                        null,
                        androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(R.id.student_nav_graph, false)
                            .build()
                    )
                    true
                }
                else -> false
            }
        }
    }


}