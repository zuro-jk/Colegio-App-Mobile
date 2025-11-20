package com.jeyix.schooljeyix.ui.admin.enrollments

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.EnrollmentResponse
import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.EnrollmentStatus
import com.jeyix.schooljeyix.databinding.ItemEnrollmentAdminBinding

class EnrollmentsAdapter(
    private val onEnrollmentClick: (EnrollmentResponse) -> Unit
) : ListAdapter<EnrollmentResponse, EnrollmentsAdapter.EnrollmentViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnrollmentViewHolder {
        val binding = ItemEnrollmentAdminBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EnrollmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EnrollmentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class EnrollmentViewHolder(private val binding: ItemEnrollmentAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(enrollment: EnrollmentResponse) {
            val context = itemView.context

            // 1. Datos básicos
            binding.tvStudentName.text = enrollment.student.fullName
            binding.tvGradeInfo.text = "${enrollment.student.gradeLevel} • ${enrollment.academicYear}"

            val (statusText, colorRes) = when (enrollment.status) {
                EnrollmentStatus.PAID -> "PAGADO" to R.color.success
                EnrollmentStatus.PENDING_PAYMENT -> "PENDIENTE" to R.color.warning
                EnrollmentStatus.CANCELLED -> "CANCELADO" to R.color.error
            }

            binding.chipStatus.text = statusText
            binding.chipStatus.chipBackgroundColor = ColorStateList.valueOf(
                ContextCompat.getColor(context, colorRes)
            )

            itemView.setOnClickListener { onEnrollmentClick(enrollment) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<EnrollmentResponse>() {
        override fun areItemsTheSame(oldItem: EnrollmentResponse, newItem: EnrollmentResponse) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: EnrollmentResponse, newItem: EnrollmentResponse) = oldItem == newItem
    }
}