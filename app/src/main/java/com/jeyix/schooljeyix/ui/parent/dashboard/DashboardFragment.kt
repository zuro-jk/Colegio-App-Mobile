package com.jeyix.schooljeyix.ui.parent.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
        binding.tvWelcome.text = name
        Glide.with(requireContext())
            .load(avatarUrl)
            .transform(CircleCrop())
            .placeholder(R.drawable.ic_parent_avatar_24)
            .error(R.drawable.ic_parent_avatar_24)
            .into(binding.ivAvatar)
    }

    private fun updateFinancialUI(nextPayment: PaymentSummary?, overdueCount: Int) {
        val context = requireContext()

        if (overdueCount > 0) {
            binding.layoutOverduePayment.isVisible = true
            binding.tvOverdueCount.text = "Tienes $overdueCount pago(s) vencido(s)"

            binding.tvFinanceTitle.text = "¡Atención Requerida!"
            binding.tvFinanceTitle.setTextColor(ContextCompat.getColor(context, R.color.error))
        } else {
            binding.layoutOverduePayment.isVisible = false
            binding.tvFinanceTitle.setTextColor(ContextCompat.getColor(context, R.color.primary))
        }

        // 2. Manejo del Próximo Pago
        if (nextPayment != null) {
            binding.tvFinanceTitle.text = if (overdueCount > 0) "¡Atención Requerida!" else "Próximo Vencimiento"

            binding.tvNextPaymentInfo.text = "S/ ${nextPayment.amount}"
            binding.tvNextPaymentInfo.setTextColor(ContextCompat.getColor(context, R.color.primary))
            binding.tvNextPaymentLabel.text = "Matrícula - ${nextPayment.studentName}"

            binding.divider.isVisible = true
            binding.ivCalendar.isVisible = true
            binding.tvNextPaymentDate.isVisible = true

            try {
                val date = LocalDate.parse(nextPayment.dueDate)
                val formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM", Locale("es", "ES"))
                val fechaFormateada = date.format(formatter)

                if (date.isBefore(LocalDate.now())) {
                    binding.tvNextPaymentDate.text = "Venció el $fechaFormateada"
                    binding.tvNextPaymentDate.setTextColor(ContextCompat.getColor(context, R.color.error))
                    binding.ivCalendar.setColorFilter(ContextCompat.getColor(context, R.color.error))
                } else {
                    binding.tvNextPaymentDate.text = "Vence el $fechaFormateada"
                    binding.tvNextPaymentDate.setTextColor(ContextCompat.getColor(context, R.color.primary))
                    binding.ivCalendar.setColorFilter(ContextCompat.getColor(context, R.color.primary))
                }

            } catch (e: Exception) {
                binding.tvNextPaymentDate.text = "Vence: ${nextPayment.dueDate}"
            }
        } else {
            if (overdueCount == 0) {
                binding.tvFinanceTitle.text = "Estado de Cuenta"
                binding.tvNextPaymentInfo.text = "¡Todo al día!"
                binding.tvNextPaymentInfo.setTextColor(ContextCompat.getColor(context, R.color.success))
                binding.tvNextPaymentLabel.text = "No tienes pagos pendientes."

                binding.divider.isVisible = false
                binding.ivCalendar.isVisible = false
                binding.tvNextPaymentDate.isVisible = false
            }
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