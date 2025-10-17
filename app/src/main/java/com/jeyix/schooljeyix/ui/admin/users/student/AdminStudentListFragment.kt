package com.jeyix.schooljeyix.ui.admin.users.student

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.databinding.FragmentAdminStudentListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AdminStudentListFragment : Fragment(R.layout.fragment_admin_student_list) {

    private var _binding: FragmentAdminStudentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdminStudentListViewModel by viewModels()
    private lateinit var studentAdapter: AdminStudentListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminStudentListBinding.bind(view)

        setupRecyclerView()
        observeUiState()
    }


    private fun setupRecyclerView() {
        studentAdapter = AdminStudentListAdapter { user, action ->
            when (action) {
                "edit" -> Toast.makeText(context, "Editar: ${user.fullName}", Toast.LENGTH_SHORT).show()
                "delete" -> Toast.makeText(context, "Eliminar: ${user.fullName}", Toast.LENGTH_SHORT).show()
            }
        }
        binding.recyclerView.adapter = studentAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                Log.d("AdminStudentDebug", "Nuevo estado de UI: $state")
                binding.progressBar.isVisible = state.isLoading
                studentAdapter.submitList(state.students)
                state.error?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}