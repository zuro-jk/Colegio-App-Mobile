package com.jeyix.schooljeyix.ui.parent.payments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.databinding.ItemPaymentBinding
import com.jeyix.schooljeyix.domain.model.PaymentItem
import java.time.format.DateTimeFormatter
import java.util.Locale

class PaymentsAdapter(private val onPayClick: (PaymentItem) -> Unit) :
    ListAdapter<PaymentItem, PaymentsAdapter.PaymentViewHolder>(PaymentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val binding = ItemPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PaymentViewHolder(private val binding: ItemPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(payment: PaymentItem) {
            val context = itemView.context

            binding.tvPaymentConcept.text = payment.concept
            binding.tvPaymentAmount.text = "S/ ${payment.amount}"

            val formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy", Locale("es", "ES"))
            binding.tvStudentName.text = "Para: ${payment.studentName} ・ Vence ${payment.dueDate.format(formatter)}"

            when {
                payment.isPaid -> {
                    binding.tvStatusTag.text = "PAGADO"
                    binding.tvStatusTag.background = ContextCompat.getDrawable(context, R.drawable.bg_status_paid)

                    binding.btnPayNow.visibility = View.GONE
                }
                else -> {
                    binding.tvStatusTag.text = if(payment.isOverdue) "VENCIDO" else "PENDIENTE"
                    binding.tvStatusTag.background = ContextCompat.getDrawable(context,
                        if(payment.isOverdue) R.drawable.bg_status_overdue else R.drawable.bg_status_pending)

                    binding.btnPayNow.visibility = View.VISIBLE
                    binding.btnPayNow.setOnClickListener { onPayClick(payment) }
                }
            }

            if (!payment.isPaid) {
                itemView.setOnClickListener { onPayClick(payment) }
                itemView.isClickable = true
            } else {
                itemView.isClickable = false
                itemView.setOnClickListener(null)
            }
        }
    }

    class PaymentDiffCallback : DiffUtil.ItemCallback<PaymentItem>() {
        override fun areItemsTheSame(old: PaymentItem, new: PaymentItem) = old.id == new.id
        override fun areContentsTheSame(old: PaymentItem, new: PaymentItem) = old == new
    }
}