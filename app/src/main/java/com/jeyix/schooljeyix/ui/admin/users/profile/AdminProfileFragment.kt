package com.jeyix.schooljeyix.ui.admin.users.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.data.remote.feature.auth.response.UserProfileResponse
import com.jeyix.schooljeyix.databinding.FragmentAdminProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdminProfileFragment : Fragment(R.layout.fragment_admin_profile) {

    private var _binding: FragmentAdminProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdminProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminProfileBinding.bind(view)

        setupListeners()
        observeState()
    }

    private fun setupListeners() {
        binding.btnEditProfile.setOnClickListener {
            Toast.makeText(context, "Próximamente: Editar Perfil", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                state.user?.let { userProfile ->
                    populateUserData(userProfile)
                }

                if (state.error != null) {
                    Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun populateUserData(user: UserProfileResponse) {
        binding.apply {
            val displayName = user.fullName ?: "${user.firstName} ${user.lastName}"
            tvFullName.text = displayName

            val roleName = user.roles?.firstOrNull()?.replace("ROLE_", "") ?: "USUARIO"
            chipRole.text = roleName

            val avatarUrl = if (!user.profileImageUrl.isNullOrBlank()) {
                user.profileImageUrl
            } else {
                "https://api.dicebear.com/7.x/avataaars/png?seed=${user.lastName}"
            }

            Glide.with(requireContext())
                .load(avatarUrl)
                .placeholder(R.drawable.ic_account_circle_24)
                .into(ivProfileImage)

            // Datos de detalle
            itemUsername.tvLabel.text = "Usuario"
            itemUsername.tvValue.text = "@${user.username}"
            itemUsername.ivIcon.setImageResource(R.drawable.ic_account_circle_24)

            itemEmail.tvLabel.text = "Correo Electrónico"
            itemEmail.tvValue.text = user.email ?: "No disponible"
            itemEmail.ivIcon.setImageResource(R.drawable.ic_email_24)

            itemPhone.tvLabel.text = "Teléfono"
            itemPhone.tvValue.text = user.phone ?: "No registrado"
            itemPhone.ivIcon.setImageResource(R.drawable.ic_phone_enabled_24)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}