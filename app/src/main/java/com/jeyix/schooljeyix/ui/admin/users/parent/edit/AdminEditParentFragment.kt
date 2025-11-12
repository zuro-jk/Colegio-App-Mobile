package com.jeyix.schooljeyix.ui.admin.users.parent.edit

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.databinding.FragmentAdminEditParentBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jeyix.schooljeyix.data.remote.feature.parent.request.UpdateParentRequest
import com.jeyix.schooljeyix.data.remote.feature.parent.response.detail.ParentDetailResponse
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdminEditParentFragment : Fragment(R.layout.fragment_admin_edit_parent) {

    private var _binding: FragmentAdminEditParentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminEditParentViewModel by viewModels()
    private val args: AdminEditParentFragmentArgs by navArgs()

    private var tempImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.entries.all { it.value }
            if (allGranted) {
                showImagePicker()
            } else {
                Toast.makeText(context, "Se requieren permisos de cámara y galería", Toast.LENGTH_SHORT).show()
            }
        }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    viewModel.updateProfileImage(uri)
                }
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                tempImageUri?.let { uri ->
                    viewModel.updateProfileImage(uri)
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminEditParentBinding.bind(view)

        setupClickListeners()
        observeUiState()
    }

    private fun setupClickListeners() {
        binding.btnSaveChanges.setOnClickListener {
            saveChanges()
        }

        binding.fabChangePhoto.setOnClickListener {
            checkPermissionsAndShowPicker()
        }
        binding.ivProfilePicture.setOnClickListener {
            checkPermissionsAndShowPicker()
        }

    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                binding.btnSaveChanges.isEnabled = !state.isLoading
                binding.btnSaveChanges.text = if (state.isLoading) "Guardando..." else "Guardar Cambios"

                if (state.parent != null && !state.isFormPopulated) {
                    populateForm(state.parent)
                    viewModel.onFormPopulated()
                }

                val avatarUrl = if (!state.parent?.user?.profileImageUrl.isNullOrBlank()) {
                    state.parent?.user?.profileImageUrl
                } else {
                    "https"
                }
                Glide.with(this@AdminEditParentFragment)
                    .load(avatarUrl)
                    .placeholder(R.drawable.ic_parent_avatar_24)
                    .error(R.drawable.ic_parent_avatar_24)
                    .circleCrop()
                    .into(binding.ivProfilePicture)


                if (state.isUpdateSuccessful) {
                    Toast.makeText(context, "Padre actualizado correctamente", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }

                state.error?.let {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    viewModel.errorShown() // Limpiar el error
                }
            }
        }
    }

    private fun populateForm(parent: ParentDetailResponse) {
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
    }

    private fun saveChanges() {
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim().ifEmpty { null }

        val currentUsername = viewModel.uiState.value.parent?.user?.username
        if (currentUsername == null) {
            Toast.makeText(context, "No se pueden guardar cambios, datos no cargados.", Toast.LENGTH_SHORT).show()
            return
        }

        val request = UpdateParentRequest(
            username = currentUsername,
            firstName = firstName,
            lastName = lastName,
            email = email,
            phone = phone
        )
        viewModel.saveChanges(request)
    }

    private fun checkPermissionsAndShowPicker() {
        val permissionsToRequest = mutableListOf<String>()
        permissionsToRequest.add(Manifest.permission.CAMERA)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        val permissionsNotGranted = permissionsToRequest.filter {
            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsNotGranted.isEmpty()) {
            showImagePicker()
        } else {
            requestPermissionLauncher.launch(permissionsNotGranted.toTypedArray())
        }
    }

    private fun showImagePicker() {
        val options = arrayOf("Abrir Galería", "Tomar Foto")
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Seleccionar Imagen")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> launchGallery()
                    1 -> launchCamera()
                }
                dialog.dismiss()
            }
            .show()
    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun launchCamera() {
        tempImageUri = createImageUri()
        tempImageUri?.let {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, it)
            }
            cameraLauncher.launch(intent)
        }
    }

    private fun createImageUri(): Uri? {
        val contentResolver = requireContext().contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "parent_profile_${System.currentTimeMillis()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/SchoolJeyix")
            }
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}