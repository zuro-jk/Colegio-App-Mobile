package com.jeyix.schooljeyix.ui.admin.users.parent.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.databinding.FragmentAdminEditParentBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdminEditParentFragment : Fragment(R.layout.fragment_admin_edit_parent) {

    private var _binding: FragmentAdminEditParentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminEditParentViewModel by viewModels()
    private val args: AdminEditParentFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminEditParentBinding.bind(view)

        val parentId = args.parentId

        binding.btnSaveChanges.setOnClickListener {
        }

        observeUiState()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->

                state.parent?.let { parent ->
                    val user = parent.user


                    binding.etFirstName.setText(user.firstName)
                    binding.etLastName.setText(user.lastName)
                    binding.etEmail.setText(user.email)
                    binding.etPhone.setText(user.phone)

                    binding.switchEnabled.isChecked = user.enabled
                    binding.chipRole.text = "Rol: PADRE"

                    if (user.emailVerified) {
                        binding.chipEmailVerified.text = "Email Verificado"
                        binding.chipEmailVerified.setChipIconTintResource(R.color.success)
                    } else {
                        binding.chipEmailVerified.text = "Email Sin Verificar"
                        binding.chipEmailVerified.setChipIconTintResource(R.color.warning)
                    }

//                    val avatarUrl = if (!user.profileImageUrl.isNullOrBlank()) {
//                        user.profileImageUrl
//                    } else {
//                        "https://www.transparentpng.com/download/user/gray-user-profile-icon-png-fP8Q1P.png"
//                    }
//
//                    Glide.with(this@AdminEditParentFragment)
//                        .load(avatarUrl)
//                        .placeholder(R.drawable.ic_parent_avatar_24)
//                        .error(R.drawable.ic_parent_avatar_24)
//                        .into(binding.ivProfilePicture)

                }

                if (state.isUpdateSuccessful) {
                    Toast.makeText(context, "Padre actualizado correctamente", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }

                state.error?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}