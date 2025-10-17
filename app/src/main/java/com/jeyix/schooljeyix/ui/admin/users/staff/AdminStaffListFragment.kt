package com.jeyix.schooljeyix.ui.admin.users.staff

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.databinding.FragmentAdminStaffListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdminStaffListFragment : Fragment(R.layout.fragment_admin_staff_list) {

    private var _binding: FragmentAdminStaffListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdminStaffListViewModel by viewModels()
    private lateinit var userAdapter: AdminUserAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminStaffListBinding.bind(view)

        setupRecyclerView()
        observeUiState()
    }

    private fun setupRecyclerView() {
        // Inicializamos el adapter genérico
        userAdapter = AdminUserAdapter { user, action ->
            when (action) {
                "edit" -> Toast.makeText(context, "Editar: ${user.fullName}", Toast.LENGTH_SHORT).show()
                "delete" -> Toast.makeText(context, "Eliminar: ${user.fullName}", Toast.LENGTH_SHORT).show()
            }
        }
        binding.recyclerView.adapter = userAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                binding.progressBar.isVisible = state.isLoading
                userAdapter.submitList(state.staff)
                state.error?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}