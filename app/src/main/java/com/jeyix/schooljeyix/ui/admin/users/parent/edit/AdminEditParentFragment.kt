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
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.databinding.FragmentAdminEditParentBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jeyix.schooljeyix.data.remote.feature.parent.request.CreateParentRequest
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

    // --- Variables de Modo ---
    private var isEditMode = false
    private var parentId: Long = 0L

    // --- Lanzadores de Permisos y Actividades ---
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
    // --- Fin de Lanzadores ---

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminEditParentBinding.bind(view)

        parentId = args.parentId
        isEditMode = (parentId != 0L)

        setupToolbar()
        setupClickListeners()
        observeUiState()
        setupUIForMode()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        if (isEditMode) {
            binding.toolbar.title = "Editar Padre"
        } else {
            binding.toolbar.title = "Nuevo Padre"
        }
    }

    private fun setupUIForMode() {
        if (isEditMode) {
            binding.tilUsername.isEnabled = false // No se puede cambiar el username
            binding.tilPassword.isVisible = false
            binding.tvPasswordHint.isVisible = false
            binding.cardStatus.isVisible = true // Mostrar el estado de la cuenta
        } else {
            binding.tilUsername.isEnabled = true
            binding.tilPassword.isVisible = true
            binding.tvPasswordHint.isVisible = false
            binding.fabChangePhoto.isVisible = false // No se puede cambiar foto sin crear
            binding.ivProfilePicture.setOnClickListener(null) // Deshabilitar clic en imagen
            binding.cardStatus.isVisible = false // No hay estado de cuenta aún
        }
    }

    private fun setupClickListeners() {
        binding.btnSaveChanges.setOnClickListener {
            saveChanges()
        }

        binding.fabChangePhoto.setOnClickListener {
            if (isEditMode) {
                checkPermissionsAndShowPicker()
            } else {
                Toast.makeText(context, "Guarda el padre antes de añadir una foto", Toast.LENGTH_SHORT).show()
            }
        }
        binding.ivProfilePicture.setOnClickListener {
            if (isEditMode) {
                checkPermissionsAndShowPicker()
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                binding.btnSaveChanges.isEnabled = !state.isLoading
                binding.btnSaveChanges.text = if (state.isLoading) "Guardando..." else "Guardar Cambios"
                binding.progressBar.isVisible = state.isLoading

                // --- ¡AQUÍ ESTÁ LA LÍNEA QUE FALTABA! ---
                // Oculta el formulario mientras carga, lo muestra cuando termina.
                binding.nestedScrollView.isVisible = !state.isLoading
                // --- FIN DE LA CORRECCIÓN ---


                // Lógica de poblar formulario (corregida)
                if (state.parent != null && !state.isFormPopulated && isEditMode) {
                    populateForm(state.parent)
                    viewModel.onFormPopulated()
                }

                // Lógica de imagen (corregida)
                val avatarUrl = state.parent?.user?.profileImageUrl
                if (!avatarUrl.isNullOrBlank()) {
                    Glide.with(this@AdminEditParentFragment)
                        .load(avatarUrl)
                        .placeholder(R.drawable.ic_parent_avatar_24)
                        .error(R.drawable.ic_parent_avatar_24)
                        .circleCrop()
                        .into(binding.ivProfilePicture)
                } else {
                    // Cargar placeholder si no hay imagen
                    Glide.with(this@AdminEditParentFragment)
                        .load(R.drawable.ic_parent_avatar_24)
                        .circleCrop()
                        .into(binding.ivProfilePicture)
                }

                if (state.isUpdateSuccessful) {
                    Toast.makeText(context, "Operación exitosa", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }

                state.error?.let {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    viewModel.errorShown()
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
        binding.etUsername.setText(user.username)

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
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (isEditMode) {
            // --- MODO EDICIÓN ---
            if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || username.isBlank()) {
                Toast.makeText(context, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show()
                return
            }
            val request = UpdateParentRequest(
                username = username,
                firstName = firstName,
                lastName = lastName,
                email = email,
                phone = phone
            )
            viewModel.updateParent(request)

        } else {
            // --- MODO CREAR ---
            if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || username.isBlank() || password.isBlank()) {
                Toast.makeText(context, "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                return
            }
            if (password.length < 6) {
                binding.tilPassword.error = "La contraseña debe tener al menos 6 caracteres"
                return
            }

            val request = CreateParentRequest(
                username = username,
                firstName = firstName,
                lastName = lastName,
                email = email,
                phone = phone,
                password = password
            )
            viewModel.createParent(request)
        }
    }

    // --- (Funciones de permisos, cámara, galería y URI) ---
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