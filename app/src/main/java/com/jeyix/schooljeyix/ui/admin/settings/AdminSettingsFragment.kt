package com.jeyix.schooljeyix.ui.admin.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.databinding.FragmentAdminSettingsBinding

class AdminSettingsFragment : Fragment(R.layout.fragment_admin_settings) {

    private var _binding: FragmentAdminSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminSettingsBinding.bind(view)

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        val currentNightMode = AppCompatDelegate.getDefaultNightMode()
        binding.switchDarkMode.isChecked = currentNightMode == AppCompatDelegate.MODE_NIGHT_YES

        val pInfo = context?.packageManager?.getPackageInfo(context!!.packageName, 0)
        val version = pInfo?.versionName
        binding.tvAppVersion.text = "SchoolJeyix v$version"
    }

    private fun setupListeners() {
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.btnLanguage.setOnClickListener {
            Toast.makeText(context, "Cambiar idioma: Próximamente", Toast.LENGTH_SHORT).show()
        }

        binding.btnChangePassword.setOnClickListener {
            Toast.makeText(context, "Navegar a cambio de contraseña", Toast.LENGTH_SHORT).show()
        }

        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "activadas" else "desactivadas"
            Toast.makeText(context, "Notificaciones $status", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}