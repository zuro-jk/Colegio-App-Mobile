package com.jeyix.schooljeyix.ui.admin.users.parent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.databinding.FragmentAdminParentListBinding
import com.jeyix.schooljeyix.ui.admin.users.UsersFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdminParentListFragment : Fragment(R.layout.fragment_admin_parent_list) {

    private var _binding: FragmentAdminParentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdminParentListViewModel by viewModels()
    private lateinit var parentAdapter: AdminParentListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminParentListBinding.bind(view)

        setupRecyclerView()
        observeUiState()
    }

    private fun setupRecyclerView() {
        parentAdapter = AdminParentListAdapter { parent, action ->
            when (action) {
                "edit" -> {
                    val navAction = UsersFragmentDirections.actionUsersFragmentToEditParentFragment(parent.id)
                    findNavController().navigate(navAction)
                }
                "delete" -> Toast.makeText(context, "Eliminar: ${parent.user.fullName}", Toast.LENGTH_SHORT).show()
            }
        }
        binding.recyclerView.adapter = parentAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                binding.progressBar.isVisible = state.isLoading
                parentAdapter.submitList(state.parents)
                state.error?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}