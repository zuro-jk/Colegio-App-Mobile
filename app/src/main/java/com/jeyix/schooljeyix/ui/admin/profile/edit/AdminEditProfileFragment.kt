package com.jeyix.schooljeyix.ui.admin.profile.edit

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.databinding.FragmentAdminEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdminEditProfileFragment : Fragment(R.layout.fragment_admin_edit_profile) {

    private var _binding: FragmentAdminEditProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdminEditProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminEditProfileBinding.bind(view)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentUser.collect { user ->
                    if (user != null && binding.etFirstName.text.isNullOrEmpty()) {
                        binding.etFirstName.setText(user.firstName)
                        binding.etLastName.setText(user.lastName)
                        binding.etUsername.setText(user.username)
                        binding.etEmail.setText(user.email)
                        binding.etPhone.setText(user.phone)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.progressBar.isVisible = state.isLoading
                binding.btnSave.isEnabled = !state.isLoading

                if (state.error != null) {
                    Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
                }

                if (state.isSuccess) {
                    Toast.makeText(context, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener {
            val firstName = binding.etFirstName.text.toString().trim()
            val lastName = binding.etLastName.text.toString().trim()
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()

            if (firstName.isBlank() || lastName.isBlank() || email.isBlank()) {
                Toast.makeText(context, "Por favor completa los campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.updateProfile(firstName, lastName, username, email, phone)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



