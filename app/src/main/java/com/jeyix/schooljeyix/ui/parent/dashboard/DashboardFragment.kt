package com.jeyix.schooljeyix.ui.parent.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeyix.schooljeyix.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.StudentSummary
import com.jeyix.schooljeyix.databinding.ParentFragmentDashboardBinding
import com.jeyix.schooljeyix.domain.model.PaymentSummary
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private var _binding: ParentFragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()
    private lateinit var studentAdapter: StudentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ParentFragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeUiState()
    }

    private fun setupRecyclerView() {
        studentAdapter = StudentAdapter()
        binding.rvStudents.apply {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    Log.d("DashboardDebug", "Nuevo estado de UI recibido: $state")

                    updateWelcomeUI(state.parentName, state.parentAvatarUrl)
                    updateFinancialUI(state.nextPayment, state.overduePaymentsCount)
                    updateStudentsUI(state.students)
                    state.error?.let {
                        Log.e("DashboardDebug", "Error en el estado: $it")
                    }
                }
            }
        }
    }

    private fun updateWelcomeUI(name: String, avatarUrl: String?) {
        binding.tvWelcome.text = "¡Hola, $name!"
        Glide.with(requireContext())
            .load(avatarUrl)
            .transform(CircleCrop())
            .placeholder(R.drawable.ic_parent_avatar_24)
            .error(R.drawable.ic_parent_avatar_24)
            .into(binding.ivAvatar)
    }

    private fun updateFinancialUI(nextPayment: PaymentSummary?, overdueCount: Int) {
        binding.layoutOverduePayment.isVisible = overdueCount > 0
        if (overdueCount > 0) {
            binding.tvOverdueCount.text = "¡Atención! Tienes $overdueCount pago(s) vencido(s)."
        }

        if (nextPayment != null) {
            // CORRECCIÓN: Usamos el studentName que ahora viene en el objeto
            binding.tvNextPaymentInfo.text = "S/ ${nextPayment.amount} - ${nextPayment.studentName}"

            // CORRECCIÓN: Parseamos el String a LocalDate ANTES de formatearlo
            val formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy", Locale("es", "ES"))
            val date = LocalDate.parse(nextPayment.dueDate)
            binding.tvNextPaymentDate.text = "Vence el ${date.format(formatter)}"
        } else {
            binding.tvNextPaymentInfo.text = "No tienes pagos próximos."
            binding.tvNextPaymentDate.text = "¡Estás al día!"
        }
    }

    private fun updateStudentsUI(students: List<StudentSummary>) {
        studentAdapter.submitList(students)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}