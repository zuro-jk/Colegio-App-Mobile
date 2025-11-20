package com.jeyix.schooljeyix.ui.admin.notifications.form

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.databinding.FragmentAdminCreateAnnouncementBinding
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdminCreateAnnouncementFragment : Fragment(R.layout.fragment_admin_create_announcement) {

    private var _binding: FragmentAdminCreateAnnouncementBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateAnnouncementViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminCreateAnnouncementBinding.bind(view)

        toggleMainNavigation(false)

        setupListeners()
        observeState()
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.chipAll.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.chipParents.isChecked = false
                binding.chipStudents.isChecked = false
            }
        }
        binding.chipParents.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.chipAll.isChecked = false
        }
        binding.chipStudents.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.chipAll.isChecked = false
        }

        // Botón de Enviar
        binding.btnSend.setOnClickListener {
            val title = binding.etSubject.text.toString().trim()
            val body = binding.etMessage.text.toString().trim()

            viewModel.sendAnnouncement(
                title = title,
                body = body,
                isAll = binding.chipAll.isChecked,
                isParents = binding.chipParents.isChecked,
                isStudents = binding.chipStudents.isChecked
            )
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        if (result.data == null && result.message == null) {
                            binding.progressBar.isVisible = false
                            binding.btnSend.text = "Enviar Comunicado"
                            binding.btnSend.isEnabled = true
                        } else {
                            // Cargando real
                            binding.progressBar.isVisible = true
                            binding.btnSend.text = ""
                            binding.btnSend.isEnabled = false
                        }
                    }
                    is Resource.Success -> {
                        binding.progressBar.isVisible = false
                        Toast.makeText(context, "Comunicado enviado con éxito", Toast.LENGTH_LONG).show()
                        findNavController().popBackStack()
                    }
                    is Resource.Error -> {
                        binding.progressBar.isVisible = false
                        binding.btnSend.text = "Enviar Comunicado"
                        binding.btnSend.isEnabled = true
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