package com.jeyix.schooljeyix.ui.admin.users.student.form

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
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.data.remote.feature.grade.response.GradeResponse
import com.jeyix.schooljeyix.data.remote.feature.parent.response.ParentResponse
import com.jeyix.schooljeyix.data.remote.feature.section.response.SectionResponse
import com.jeyix.schooljeyix.data.remote.feature.student.request.CreateStudentRequest
import com.jeyix.schooljeyix.data.remote.feature.student.request.UpdateStudentRequest
import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse
import com.jeyix.schooljeyix.databinding.FragmentAdminStudentFormBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AdminStudentFormFragment : Fragment(R.layout.fragment_admin_student_form) {
    private var _binding: FragmentAdminStudentFormBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdminStudentFormViewModel by viewModels()
    private val args: AdminStudentFormFragmentArgs by navArgs()

    private val parentMap = mutableMapOf<String, Long>()
    private val gradeMap = mutableMapOf<String, Long>()
    private val sectionMap = mutableMapOf<String, Long>()

    private var selectedParentId: Long? = null
    private var selectedGradeId: Long? = null
    private var selectedSectionId: Long? = null

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
        _binding = FragmentAdminStudentFormBinding.bind(view)

        setupToolbar()
        setupClickListeners()
        observeUiState()
    }

    private fun setupToolbar() {
        if (args.studentId != null) {
            binding.toolbar.title = "Editar Estudiante"
            binding.tilPassword.hint = "Contraseña (dejar en blanco para no cambiar)"
            binding.tvPasswordHint.isVisible = true
        } else {
            binding.toolbar.title = "Nuevo Estudiante"
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

                state.studentDetails?.user?.profileImageUrl?.let { imageUrl ->
                    Glide.with(requireContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_account_circle_24)
                        .circleCrop()
                        .into(binding.ivProfile)
                }

                if (state.parentList.isNotEmpty()) {
                    setupParentDropdown(state.parentList)
                }
                if (state.gradeList.isNotEmpty()) {
                    setupGradeDropdown(state.gradeList)
                }
                if (state.sectionList.isNotEmpty()) {
                    setupSectionDropdown(state.sectionList)
                } else {
                    binding.tilSection.isEnabled = false
                }

                if (state.studentDetails != null &&
                    !state.isFormPopulated &&
                    state.parentList.isNotEmpty() &&
                    state.gradeList.isNotEmpty()) {

                    populateForm(state.studentDetails)
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

    private fun setupParentDropdown(parents: List<ParentResponse>) {
        parentMap.clear()
        val parentNames = parents.map { parent ->
            val name = parent.user.fullName
            parentMap[name] = parent.id
            name
        }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, parentNames)
        binding.actvParent.setAdapter(adapter)

        binding.actvParent.setOnItemClickListener { _, _, position, _ ->
            val selectedName = adapter.getItem(position)
            selectedParentId = parentMap[selectedName]
        }
    }

    private fun setupGradeDropdown(grades: List<GradeResponse>) {
        gradeMap.clear()
        val gradeNames = grades.map { grade ->
            gradeMap[grade.name] = grade.id
            grade.name
        }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, gradeNames)
        binding.actvGrade.setAdapter(adapter)

        binding.actvGrade.setOnItemClickListener { _, _, position, _ ->
            val selectedName = adapter.getItem(position)
            selectedGradeId = gradeMap[selectedName]

            selectedSectionId = null
            binding.actvSection.setText("", false)
            binding.tilSection.isEnabled = false

            selectedGradeId?.let {
                viewModel.onGradeSelected(it)
            }
        }
    }

    private fun setupSectionDropdown(sections: List<SectionResponse>) {
        sectionMap.clear()
        val sectionNames = sections.map { section ->
            sectionMap[section.name] = section.id
            section.name
        }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, sectionNames)
        binding.actvSection.setAdapter(adapter)
        binding.tilSection.isEnabled = true

        binding.actvSection.setOnItemClickListener { _, _, position, _ ->
            val selectedName = adapter.getItem(position)
            selectedSectionId = sectionMap[selectedName]
        }

        val studentDetails = viewModel.uiState.value.studentDetails
        if (args.studentId != null && studentDetails != null) {
            if (sectionNames.contains(studentDetails.section)) {
                binding.actvSection.setText(studentDetails.section, false)
                selectedSectionId = sectionMap[studentDetails.section]
            }
        }
    }


    private fun populateForm(student: StudentResponse) {
        binding.etFirstName.setText(student.user.firstName)
        binding.etLastName.setText(student.user.lastName)
        binding.etUsername.setText(student.user.username)
        binding.etEmail.setText(student.user.email)
        binding.etPhone.setText(student.user.phone)

        selectedParentId = student.parentId
        val parentName = parentMap.entries.find { it.value == student.parentId }?.key
        binding.actvParent.setText(parentName, false)

        selectedGradeId = gradeMap.entries.find { it.key == student.gradeLevel }?.value
        binding.actvGrade.setText(student.gradeLevel, false)

        student.user.profileImageUrl?.let {
            Glide.with(requireContext())
                .load(it)
                .placeholder(R.drawable.ic_account_circle_24)
                .circleCrop()
                .into(binding.ivProfile)
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
            if (args.studentId == null) {
                Toast.makeText(context, "Guarda al estudiante antes de añadir una foto", Toast.LENGTH_SHORT).show()
            } else {
                checkPermissionsAndShowPicker()
            }
        }
        binding.ivProfile.setOnClickListener {
            if (args.studentId != null) {
                checkPermissionsAndShowPicker()
            }
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
            put(MediaStore.MediaColumns.DISPLAY_NAME, "student_profile_${System.currentTimeMillis()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/SchoolJeyix")
            }
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    private fun saveChanges() {
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim().ifEmpty { null }
        val password = binding.etPassword.text.toString()

        val academicYear = binding.etAcademicYear.text.toString().trim()
        val totalAmountStr = binding.etTotalAmount.text.toString().trim()
        val installmentsStr = binding.etInstallments.text.toString().trim()

        if (firstName.isBlank() || lastName.isBlank() || username.isBlank() || email.isBlank() ||
            selectedParentId == null || selectedGradeId == null || selectedSectionId == null) {
            Toast.makeText(context, "Por favor, completa todos los campos del estudiante", Toast.LENGTH_SHORT).show()
            return
        }

        if (args.studentId == null) {
            if (password.isBlank()) {
                binding.tilPassword.error = "La contraseña es obligatoria"
                return
            }
            if (academicYear.isBlank() || totalAmountStr.isBlank() || installmentsStr.isBlank()) {
                Toast.makeText(context, "Completa los datos de matrícula", Toast.LENGTH_SHORT).show()
                return
            }
        }

        if (args.studentId != null) {
            val request = UpdateStudentRequest(
                username = username,
                firstName = firstName,
                lastName = lastName,
                email = email,
                phone = phone,
                parentId = selectedParentId!!,
                sectionId = selectedSectionId!!
            )
            viewModel.updateStudent(request)

        } else {
            val totalAmount = totalAmountStr.toBigDecimalOrNull() ?: java.math.BigDecimal.ZERO
            val installments = installmentsStr.toIntOrNull() ?: 0

            val request = CreateStudentRequest(
                username = username,
                firstName = firstName,
                lastName = lastName,
                email = email,
                password = password,
                phone = phone,
                parentId = selectedParentId!!,
                sectionId = selectedSectionId!!,

                // Campos nuevos
                academicYear = academicYear,
                totalAmount = totalAmount,
                numberOfInstallments = installments
            )
            viewModel.saveStudent(request)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}