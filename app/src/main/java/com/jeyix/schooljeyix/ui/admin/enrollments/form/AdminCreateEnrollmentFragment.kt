package com.jeyix.schooljeyix.ui.admin.enrollments.form


import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse
import com.jeyix.schooljeyix.databinding.FragmentAdminCreateEnrollmentBinding
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdminCreateEnrollmentFragment : Fragment(R.layout.fragment_admin_create_enrollment) {

    private var _binding: FragmentAdminCreateEnrollmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateEnrollmentViewModel by viewModels()
    private var studentsList: List<StudentResponse> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminCreateEnrollmentBinding.bind(view)

        toggleMainNavigation(false)

        setupYearSelector()

        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        binding.btnSave.setOnClickListener {
            viewModel.createEnrollment(
                academicYear = binding.actvAcademicYear.text.toString(),
                amountStr = binding.etTotalAmount.text.toString(),
                installmentsStr = binding.etInstallments.text.toString()
            )
        }

        observeState()
        observeStudents()
    }

    private fun setupYearSelector() {
        val currentYear = java.time.Year.now().value
        val years = listOf(currentYear.toString(), (currentYear + 1).toString())

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, years)
        binding.actvAcademicYear.setAdapter(adapter)

        if (binding.actvAcademicYear.text.isEmpty()) {
            binding.actvAcademicYear.setText(years[0], false)
        }
    }

    private fun observeStudents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.studentsList.collectLatest { students ->
                studentsList = students

                val studentNames = students.map { it.user.fullName }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, studentNames)

                binding.actvStudent.setAdapter(adapter)

                binding.actvStudent.setOnItemClickListener { _, _, position, _ ->
                    val selectedName = adapter.getItem(position)
                    val selectedStudent = students.find { it.user.fullName == selectedName }

                    selectedStudent?.let {
                        viewModel.selectedStudentId = it.id
                    }
                }
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        if (result.data == null && result.message == null) {
                            binding.progressBar.isVisible = false
                            binding.btnSave.isEnabled = true
                        } else {
                            binding.progressBar.isVisible = true
                            binding.btnSave.isEnabled = false
                        }
                    }
                    is Resource.Success -> {
                        binding.progressBar.isVisible = false
                        Toast.makeText(context, "Matrícula creada exitosamente", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                    is Resource.Error -> {
                        binding.progressBar.isVisible = false
                        binding.btnSave.isEnabled = true
                        Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun toggleMainNavigation(show: Boolean) {
        val activity = requireActivity()
        val bottomNav = activity.findViewById<BottomNavigationView>(R.id.bottom_nav)
        val topAppBar = activity.findViewById<MaterialToolbar>(R.id.topAppBar)
        bottomNav?.isVisible = show
        topAppBar?.isVisible = show
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toggleMainNavigation(true)
        _binding = null
    }
}
