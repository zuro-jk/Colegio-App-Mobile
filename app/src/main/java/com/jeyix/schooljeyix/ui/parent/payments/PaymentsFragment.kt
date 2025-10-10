package com.jeyix.schooljeyix.ui.parent.payments

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.databinding.ParentFragmentPaymentsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PaymentsFragment : Fragment() {

    private var _binding: ParentFragmentPaymentsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PaymentsViewModel by viewModels()
    private lateinit var paymentsAdapter: PaymentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ParentFragmentPaymentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeUiState()
    }

    private fun setupRecyclerView() {
        paymentsAdapter = PaymentsAdapter { paymentItem ->
            Toast.makeText(context, "Iniciando pago para la cuota de ${paymentItem.concept}", Toast.LENGTH_SHORT).show()
            // Aquí llamarías al ViewModel para iniciar el flujo de pago con Mercado Pago
            // viewModel.initiatePayment(paymentItem.id)
        }
        binding.rvPayments.apply {
            adapter = paymentsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // binding.progressBar.isVisible = state.isLoading

                    binding.tvTotalDueAmount.text = "S/ ${state.totalDueThisMonth}"
                    paymentsAdapter.submitList(state.paymentItems)

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