package com.jeyix.schooljeyix.ui.parent.students

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.jeyix.schooljeyix.R
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
        observeUiState()
    }

    private fun setupRecyclerView() {
        studentAdapter = StudentGridAdapter { student ->
            Toast.makeText(context, "Viendo perfil de ${student.user.fullName}", Toast.LENGTH_SHORT).show()
        }

        binding.rvStudents.apply {
            // Usamos GridLayoutManager para crear la cuadrícula de 2 columnas
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = studentAdapter
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // binding.progressBar.isVisible = state.isLoading

                    studentAdapter.submitList(state.students)

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