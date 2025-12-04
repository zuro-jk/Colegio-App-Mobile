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
import com.jeyix.schooljeyix.databinding.FragmentAdminCreateEnrollmentBinding
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class AdminCreateEnrollmentFragment : Fragment(R.layout.fragment_admin_create_enrollment) {

    private var _binding: FragmentAdminCreateEnrollmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateEnrollmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminCreateEnrollmentBinding.bind(view)

        toggleMainNavigation(false)

        setupToolbar()
        setupYearSelector()
        setupClickListeners()

        observeStudents()
        observeState()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun setupYearSelector() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = listOf(currentYear.toString(), (currentYear + 1).toString())

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, years)
        binding.actvAcademicYear.setAdapter(adapter)

        if (binding.actvAcademicYear.text.isEmpty()) {
            binding.actvAcademicYear.setText(currentYear.toString(), false)
        }
    }

    private fun setupClickListeners() {
        binding.btnSave.setOnClickListener {
            viewModel.createEnrollment(
                academicYear = binding.actvAcademicYear.text.toString(),
                amountStr = binding.etTotalAmount.text.toString(),
                installmentsStr = binding.etInstallments.text.toString()
            )
        }
    }

    private fun observeStudents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.studentsList.collectLatest { students ->
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
                        val isLoading = result.data != null
                        if (result.message != null) {
                            setLoading(true)
                        }
                    }
                    is Resource.Success -> {
                        setLoading(false)
                        Toast.makeText(context, "Matrícula registrada correctamente", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                    is Resource.Error -> {
                        setLoading(false)
                        // ELIMINA EL IF, muestra siempre el error para depurar
                        Toast.makeText(context, result.message ?: "Error desconocido", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.btnSave.text = if (isLoading) "" else "Registrar Matrícula"
        binding.btnSave.isEnabled = !isLoading
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