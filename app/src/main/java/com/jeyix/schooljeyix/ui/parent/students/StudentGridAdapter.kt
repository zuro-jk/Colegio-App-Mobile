package com.jeyix.schooljeyix.ui.parent.students

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse
import com.jeyix.schooljeyix.databinding.ItemStudentGridBinding

class StudentGridAdapter(
    private val onStudentClick: (StudentResponse) -> Unit
) : ListAdapter<StudentResponse, StudentGridAdapter.StudentViewHolder>(StudentDiffCallback()) {

    // Una paleta de colores pastel más vibrante y moderna
    private val bgColors = listOf(
        "#E3F2FD", // Azul muy claro
        "#F3E5F5", // Púrpura muy claro
        "#E8F5E9", // Verde muy claro
        "#FFF3E0", // Naranja muy claro
        "#E0F7FA", // Cyan muy claro
        "#FFF8E1"  // Ámbar muy claro
    )

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
            binding.tvStudentName.text = student.user.fullName
            binding.tvStudentGrade.text = student.gradeLevel

            val imageSource = if (!student.user.profileImageUrl.isNullOrEmpty()) {
                student.user.profileImageUrl
            } else {
                "https://api.dicebear.com/9.x/notionists/png?seed=${student.user.username}"
            }

            Glide.with(itemView.context)
                .load(imageSource)
                .transform(CircleCrop())
                .placeholder(R.drawable.ic_student_24)
                .error(R.drawable.ic_student_24)
                .into(binding.ivStudentAvatar)

            val colorIndex = (student.id.toInt() % bgColors.size)
            binding.viewHeaderColor.setBackgroundColor(Color.parseColor(bgColors[colorIndex]))

            itemView.setOnClickListener { onStudentClick(student) }

            binding.btnMoreOptions.setOnClickListener {
                // Abrir menú o acción rápida
            }
        }
    }

    class StudentDiffCallback : DiffUtil.ItemCallback<StudentResponse>() {
        override fun areItemsTheSame(oldItem: StudentResponse, newItem: StudentResponse) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: StudentResponse, newItem: StudentResponse) = oldItem == newItem
    }
}