package com.jeyix.schooljeyix.ui.admin.enrollments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.EnrollmentStatus
import com.jeyix.schooljeyix.databinding.FragmentAdminEnrollmentsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EnrollmentsFragment : Fragment() {

    private var _binding: FragmentAdminEnrollmentsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EnrollmentsViewModel by viewModels()
    private lateinit var adapter: EnrollmentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminEnrollmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupFilters()
        setupFab()
        observeUiState()
    }

    private fun setupRecyclerView() {
        adapter = EnrollmentsAdapter { enrollment ->
            Toast.makeText(context, "Detalles de: ${enrollment.student.fullName}", Toast.LENGTH_SHORT).show()
            // Aquí navegarías al detalle de la matrícula si quisieras
        }
        binding.rvEnrollments.layoutManager = LinearLayoutManager(context)
        binding.rvEnrollments.adapter = adapter
    }

    private fun setupFilters() {
        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            viewModel.onSearchQueryChanged(text.toString())
        }

        binding.chipGroupFilters.setOnCheckedStateChangeListener { group, checkedIds ->
            val checkedId = checkedIds.firstOrNull()

            val status = when (checkedId) {
                R.id.chipPending -> EnrollmentStatus.PENDING_PAYMENT
                R.id.chipPaid -> EnrollmentStatus.PAID
                else -> null
            }

            viewModel.onFilterChanged(status)
        }
    }

    private fun setupFab() {
        binding.fabAddEnrollment.setOnClickListener {
            Toast.makeText(context, "Crear Nueva Matrícula", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->

                    adapter.submitList(state.enrollments)

                    state.error?.let {
                        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}