package com.jeyix.schooljeyix.ui.parent.payments

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.databinding.ParentFragmentPaymentsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.math.BigDecimal

@AndroidEntryPoint
class PaymentsFragment : Fragment() {

    private var _binding: ParentFragmentPaymentsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PaymentsViewModel by viewModels()
    private lateinit var paymentsAdapter: PaymentsAdapter

    private lateinit var paymentSheet: PaymentSheet

    private var currentClientSecret: String? = null
    private var currentPaymentId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ParentFragmentPaymentsBinding.inflate(inflater, container, false)

        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeUiState()
        observeEvents()

        binding.btnPayAll.setOnClickListener {
            val state = viewModel.uiState.value
            val enrollmentId = state.paymentItems.firstOrNull()?.enrollmentId

            if (enrollmentId != null) {
                if (state.isDiscountActive) {
                    viewModel.onPayAllClicked(enrollmentId)
                } else {
                    Toast.makeText(context, "Activa el descuento para pagar el total anual", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "No hay deudas pendientes", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPayments()
    }

    private fun setupRecyclerView() {
        paymentsAdapter = PaymentsAdapter { paymentItem ->
            currentPaymentId = paymentItem.id
            viewModel.onPayClicked(paymentItem.id)
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

                    val isAllPaid = state.totalDue.compareTo(BigDecimal.ZERO) == 0 && state.paymentItems.isNotEmpty()

                    if (isAllPaid) {
                        showSuccessState()
                    } else {
                        showPaymentState(state)
                    }

                    state.error?.let {
                        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun showSuccessState() {
        binding.cardSummary.visibility = View.GONE
        binding.rvPayments.visibility = View.GONE

        binding.layoutAllPaid.visibility = View.VISIBLE
    }

    private fun showPaymentState(state: PaymentsUiState) {
        binding.layoutAllPaid.visibility = View.GONE

        binding.cardSummary.visibility = View.VISIBLE

        paymentsAdapter.submitList(state.paymentItems)

        binding.switchDiscount.setOnCheckedChangeListener(null)

        binding.switchDiscount.isEnabled = state.canApplyDiscount
        if (!state.canApplyDiscount) {
            binding.tvDiscountLabel.text = "Descuento no disponible"
            binding.switchDiscount.isChecked = false
        } else {
            binding.tvDiscountLabel.text = "Aplicar Dscto. Anual"
            binding.switchDiscount.isChecked = state.isDiscountActive
        }

        binding.switchDiscount.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleDiscount(isChecked)
        }

        updateSummaryCardContent(state)
    }

    private fun updateSummaryCardContent(state: PaymentsUiState) {
        val context = requireContext()

        if (state.isDiscountActive && state.canApplyDiscount) {
            binding.tvOriginalAmount.text = "S/ ${state.totalDue}"
            binding.tvOriginalAmount.paintFlags = binding.tvOriginalAmount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.tvOriginalAmount.visibility = View.VISIBLE

            binding.tvTotalDueAmount.text = "S/ ${state.discountedTotal}"
            binding.tvTotalDueAmount.setTextColor(ContextCompat.getColor(context, R.color.accent_teal))

            binding.chipSavings.text = "Ahorras S/ ${state.discountAmount}"
            binding.chipSavings.visibility = View.VISIBLE

            binding.btnPayAll.text = "Pagar S/ ${state.discountedTotal}"

            binding.rvPayments.visibility = View.GONE

        } else {
            binding.tvOriginalAmount.visibility = View.GONE
            binding.chipSavings.visibility = View.GONE

            binding.tvTotalDueAmount.text = "S/ ${state.totalDue}"
            binding.tvTotalDueAmount.setTextColor(ContextCompat.getColor(context, R.color.white))

            binding.btnPayAll.text = "Pagar Todo"

            binding.rvPayments.visibility = View.VISIBLE
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.paymentEvent.collectLatest { event ->
                    when (event) {
                        is PaymentsViewModel.PaymentEvent.LaunchStripeSheet -> {
                            currentClientSecret = event.stripeData.paymentIntentClientSecret
                            presentPaymentSheet(event.stripeData.paymentIntentClientSecret, event.stripeData.publishableKey)
                        }
                        is PaymentsViewModel.PaymentEvent.ShowError -> {
                            Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                        }
                        is PaymentsViewModel.PaymentEvent.ShowMessage -> {
                            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun presentPaymentSheet(clientSecret: String, publishableKey: String) {
        PaymentConfiguration.init(requireContext(), publishableKey)

        paymentSheet.presentWithPaymentIntent(
            clientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = "School Jeyix",
            )
        )
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                Toast.makeText(context, "Pago cancelado", Toast.LENGTH_SHORT).show()
            }
            is PaymentSheetResult.Failed -> {
                Toast.makeText(context, "Error: ${paymentSheetResult.error.localizedMessage}", Toast.LENGTH_LONG).show()
            }
            is PaymentSheetResult.Completed -> {
                val secret = currentClientSecret

                if (secret != null) {
                    val paymentIntentId = secret.split("_secret_")[0]

                    if (viewModel.isProcessingBulkPayment) {
                        viewModel.confirmBulkPayment(paymentIntentId)
                    } else {
                        val paymentId = currentPaymentId
                        if (paymentId != null) {
                            viewModel.confirmPayment(paymentId, paymentIntentId)
                        }
                    }
                } else {
                    Toast.makeText(context, "Error interno: Datos perdidos", Toast.LENGTH_SHORT).show()
                }
            }
        }
        currentClientSecret = null
        currentPaymentId = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}