package com.jeyix.schooljeyix.ui.parent.students

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse
import com.jeyix.schooljeyix.databinding.ItemStudentGridBinding

class StudentGridAdapter(
    private val onStudentClick: (StudentResponse) -> Unit
) : ListAdapter<StudentResponse, StudentGridAdapter.StudentViewHolder>(StudentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentGridBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class StudentViewHolder(private val binding: ItemStudentGridBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(student: StudentResponse) {
            binding.tvStudentName.text = student.fullName
            binding.tvStudentGrade.text = student.gradeLevel

//            Glide.with(itemView.context)
//                .load(student.prof)
//                .placeholder(R.drawable.ic_student_24)
//                .error(R.drawable.ic_student_24)
//                .into(binding.ivStudentAvatar)

            binding.chipStatus.text = "Al día"

            itemView.setOnClickListener {
                onStudentClick(student)
            }
        }
    }

    class StudentDiffCallback : DiffUtil.ItemCallback<StudentResponse>() {
        override fun areItemsTheSame(oldItem: StudentResponse, newItem: StudentResponse): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: StudentResponse, newItem: StudentResponse): Boolean {
            return oldItem == newItem
        }
    }
}