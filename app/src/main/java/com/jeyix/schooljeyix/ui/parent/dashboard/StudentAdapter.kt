package com.jeyix.schooljeyix.ui.parent.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.data.remote.feature.enrollment.response.StudentSummary

import com.jeyix.schooljeyix.databinding.ItemStudentSummaryBinding
class StudentAdapter: ListAdapter<StudentSummary, StudentAdapter.StudentViewHolder>(StudentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentSummaryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StudentViewHolder(binding)
    }

    // Vincula los datos de un estudiante con las vistas del ViewHolder
    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // ViewHolder que contiene las referencias a las vistas de un item
    inner class StudentViewHolder(private val binding: ItemStudentSummaryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(student: StudentSummary) {
            binding.tvStudentName.text = student.fullName
            binding.tvStudentGrade.text = student.gradeLevel

            Glide.with(itemView.context)
                .load(student.profileImageUrl)
                .placeholder(R.drawable.ic_student_24)
                .error(R.drawable.ic_student_24)
                .into(binding.ivStudentAvatar)
        }
    }

    class StudentDiffCallback : DiffUtil.ItemCallback<StudentSummary>() {
        override fun areItemsTheSame(oldItem: StudentSummary, newItem: StudentSummary): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: StudentSummary, newItem: StudentSummary): Boolean {
            return oldItem == newItem
        }
    }
}