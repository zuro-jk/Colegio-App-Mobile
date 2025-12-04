package com.jeyix.schooljeyix.ui.parent.students

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.jeyix.schooljeyix.databinding.ParentFragmentStudentsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class StudentsFragment : Fragment() {

    private var _binding: ParentFragmentStudentsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StudentsViewModel by viewModels()
    private lateinit var studentAdapter: StudentGridAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ParentFragmentStudentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchView()
        observeUiState()
    }

    private fun setupSearchView() {
        binding.svStudents.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchStudent(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchStudent(newText ?: "")
                return true
            }
        })
    }

    private fun setupRecyclerView() {
        studentAdapter = StudentGridAdapter { student ->
            Toast.makeText(context, "Seleccionado: ${student.user.fullName}", Toast.LENGTH_SHORT).show()
        }

        binding.rvStudents.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = studentAdapter
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->

                    binding.progressBar.isVisible = state.isLoading

                    if (!state.isLoading) {
                        if (state.students.isEmpty()) {
                            binding.rvStudents.isVisible = false
                            binding.layoutEmptyState.isVisible = true
                        } else {
                            binding.rvStudents.isVisible = true
                            binding.layoutEmptyState.isVisible = false
                            studentAdapter.submitList(state.students)
                        }
                    }

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