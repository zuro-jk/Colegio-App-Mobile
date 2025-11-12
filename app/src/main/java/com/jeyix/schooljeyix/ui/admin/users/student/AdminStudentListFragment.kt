package com.jeyix.schooljeyix.ui.admin.users.student

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.databinding.FragmentAdminStudentListBinding
import com.jeyix.schooljeyix.ui.admin.users.SearchableFragment
import com.jeyix.schooljeyix.ui.admin.users.UsersFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AdminStudentListFragment : Fragment(R.layout.fragment_admin_student_list), SearchableFragment {

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

    override fun onResume() {
        super.onResume()
        viewModel.loadStudents()
    }


    private fun setupRecyclerView() {
        studentAdapter = AdminStudentListAdapter { user, action ->
            when (action) {
                "edit" -> {
                    val actionDetail = UsersFragmentDirections
                        .actionNavAdminUsersToAdminStudentFormFragment(user.id.toString())

                    parentFragment?.parentFragment?.findNavController()?.navigate(actionDetail)
                }
                "delete" -> {
                    Toast.makeText(context, "Eliminar: ${user.user.fullName}", Toast.LENGTH_SHORT).show()
                }
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

    override fun onSearchQuery(query: String) {
        viewModel.search(query)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}