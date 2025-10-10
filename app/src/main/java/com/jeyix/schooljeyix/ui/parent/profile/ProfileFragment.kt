package com.jeyix.schooljeyix.ui.parent.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.databinding.ParentFragmentProfileBinding
import com.jeyix.schooljeyix.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: ParentFragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ParentFragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOptionsAndListeners()
        observeUiState()
    }

    private fun setupOptionsAndListeners() {
        binding.optionEditProfile.apply {
            ivOptionIcon.setImageResource(R.drawable.ic_edit_24)
            tvOptionText.text = "Editar Perfil"
            root.setOnClickListener {
                Toast.makeText(context, "Editar Perfil (Próximamente)", Toast.LENGTH_SHORT).show()
            }
        }

        binding.optionSecurity.apply {
            ivOptionIcon.setImageResource(R.drawable.ic_security_24)
            tvOptionText.text = "Seguridad de la Cuenta"
            root.setOnClickListener {
                Toast.makeText(context, "Seguridad (Próximamente)", Toast.LENGTH_SHORT).show()
            }
        }

        // --- Configuración de la Card "Aplicación" ---
        binding.optionNotifications.apply {
            ivOptionIcon.setImageResource(R.drawable.ic_notifications_24)
            tvOptionText.text = "Preferencias de Notificación"
            root.setOnClickListener {
                Toast.makeText(context, "Notificaciones (Próximamente)", Toast.LENGTH_SHORT).show()
            }
        }

        binding.optionHelp.apply {
            ivOptionIcon.setImageResource(R.drawable.ic_help_24)
            tvOptionText.text = "Ayuda y Soporte"
            root.setOnClickListener {
                Toast.makeText(context, "Ayuda (Próximamente)", Toast.LENGTH_SHORT).show()
            }
        }

        // --- Configuración del Botón de Logout ---
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    state.userProfile?.let { user ->
                        binding.tvProfileName.text = user.fullName

                        Glide.with(this@ProfileFragment)
                            .load(user.profileImageUrl)
                            .placeholder(R.drawable.ic_parent_avatar_24)
                            .error(R.drawable.ic_parent_avatar_24)
                            .into(binding.ivProfileAvatar)
                    }

                    if (state.isLoggedOut) {
                        navigateToLogin()
                    }
                }
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(requireActivity(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}