package com.jeyix.schooljeyix.ui.admin.users.staff.form

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.data.remote.feature.auth.response.UserProfileResponse
import com.jeyix.schooljeyix.data.remote.feature.users.request.CreateAdminRequest
import com.jeyix.schooljeyix.data.remote.feature.users.request.UpdateUserRequest
import com.jeyix.schooljeyix.databinding.FragmentAdminStaffFormBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class AdminStaffFormFragment : Fragment(R.layout.fragment_admin_staff_form) {

    private var _binding: FragmentAdminStaffFormBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdminStaffFormViewModel by viewModels()
    private val args: AdminStaffFormFragmentArgs by navArgs()

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
        _binding = FragmentAdminStaffFormBinding.bind(view)

        setupToolbar()
        setupClickListeners()
        observeUiState()
    }

    private fun setupToolbar() {
        if (args.userId != 0L) { // Modo Editar
            binding.toolbar.title = "Editar Personal"
            binding.tilPassword.hint = "Contraseña (dejar en blanco para no cambiar)"
            binding.tvPasswordHint.isVisible = true
        } else { // Modo Crear
            binding.toolbar.title = "Nuevo Personal"
            binding.tilPassword.hint = "Contraseña"
            binding.tvPasswordHint.isVisible = false
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                binding.progressBar.isVisible = state.isLoading || state.isSaving
                binding.nestedScrollView.isVisible = !state.isLoading
                binding.btnSave.isEnabled = !state.isSaving
                binding.btnSave.text = if (state.isSaving) "Guardando..." else "Guardar Cambios"

                state.userDetails?.profileImageUrl?.let { imageUrl ->
                    Glide.with(requireContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_account_circle_24)
                        .circleCrop()
                        .into(binding.ivProfile)
                }

                if (state.userDetails != null && !state.isFormPopulated) {
                    populateForm(state.userDetails)
                    viewModel.onFormPopulated()
                }

                state.error?.let {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    viewModel.errorShown()
                }

                if (state.finishSaving) {
                    Toast.makeText(context, "¡Guardado exitosamente!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun populateForm(user: UserProfileResponse) {
        binding.etFirstName.setText(user.firstName)
        binding.etLastName.setText(user.lastName)
        binding.etUsername.setText(user.username)
        binding.etEmail.setText(user.email)
        binding.etPhone.setText(user.phone)
        binding.switchEnabled.isChecked = user.enabled == true

        user.profileImageUrl?.let {
            Glide.with(requireContext())
                .load(it)
                .placeholder(R.drawable.ic_account_circle_24)
                .circleCrop()
                .into(binding.ivProfile)
        }

        user.roles?.forEach { roleName ->
            when (roleName) {
                "ROLE_ADMIN" -> binding.chipRoleAdmin.isChecked = true
                "ROLE_TEACHER" -> binding.chipRoleTeacher.isChecked = true
                "ROLE_ACCOUNTANT" -> binding.chipRoleAccountant.isChecked = true
            }
        }
    }

    private fun setupClickListeners() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSave.setOnClickListener {
            saveChanges()
        }

        binding.fabEditImage.setOnClickListener {
            if (args.userId == 0L) {
                Toast.makeText(context, "Guarda el usuario antes de añadir una foto", Toast.LENGTH_SHORT).show()
            } else {
                checkPermissionsAndShowPicker()
            }
        }
        binding.ivProfile.setOnClickListener {
            if (args.userId != 0L) {
                checkPermissionsAndShowPicker()
            }
        }
    }

    private fun saveChanges() {
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim().ifEmpty { null }
        val password = binding.etPassword.text.toString()
        val isEnabled = binding.switchEnabled.isChecked

        // Leer roles seleccionados del ChipGroup
        val selectedRoles = binding.chipGroupRoles.children
            .filter { (it as Chip).isChecked }
            .map { "ROLE_${(it as Chip).text.toString().uppercase(Locale.ROOT)}" }
            .toSet()

        if (firstName.isBlank() || lastName.isBlank() || username.isBlank() || email.isBlank()) {
            Toast.makeText(context, "Por favor, completa los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedRoles.isEmpty()) {
            Toast.makeText(context, "Debes seleccionar al menos un rol", Toast.LENGTH_SHORT).show()
            return
        }

        if (args.userId != 0L) {
            val request = UpdateUserRequest(
                username = username,
                firstName = firstName,
                lastName = lastName,
                email = email,
                phone = phone,
                enabled = isEnabled,
                roles = selectedRoles
            )
            viewModel.updateUser(request)

        } else {
            if (password.isBlank()) {
                binding.tilPassword.error = "La contraseña es obligatoria para crear"
                return
            } else {
                binding.tilPassword.error = null
            }

            val request = CreateAdminRequest(
                username = username,
                firstName = firstName,
                lastName = lastName,
                email = email,
                password = password,
                phone = phone,
                enabled = isEnabled,
                roles = selectedRoles
            )
            viewModel.saveUser(request)
        }
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
            put(MediaStore.MediaColumns.DISPLAY_NAME, "staff_profile_${System.currentTimeMillis()}.jpg")
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