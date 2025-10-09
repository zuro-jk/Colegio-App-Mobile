package com.jeyix.schooljeyix.ui.parent

import android.app.ComponentCaller
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jeyix.schooljeyix.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParentMainActivity : AppCompatActivity() {

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        setIntent(intent)
        handleDeepLink(intent)
    }

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

        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
        val data: Uri? = intent?.data
        if (data != null && data.scheme == "myapp" && data.host == "payment") {

            val status = data.getQueryParameter("collection_status")
            val paymentId = data.getQueryParameter("payment_id")

            val message = when (status) {
                "approved" -> "¡Pago aprobado! ID: $paymentId"
                "rejected" -> "El pago fue rechazado."
                "in_process", "pending" -> "El pago está pendiente de aprobación."
                else -> "Estado del pago desconocido."
            }

            Toast.makeText(this, message, Toast.LENGTH_LONG).show()

            intent.data = null
        }
    }
}