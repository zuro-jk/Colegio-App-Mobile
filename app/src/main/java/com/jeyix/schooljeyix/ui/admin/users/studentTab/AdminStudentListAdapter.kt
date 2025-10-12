package com.jeyix.schooljeyix.ui.admin.users.studentTab

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.data.remote.feature.student.response.StudentResponse
import com.jeyix.schooljeyix.databinding.ItemUserAdminBinding

class AdminStudentListAdapter(
    private val onOptionClick: (user: StudentResponse, action: String) -> Unit
) : ListAdapter<StudentResponse, AdminStudentListAdapter.StudentViewHolder>(StudentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemUserAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class StudentViewHolder(private val binding: ItemUserAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(student: StudentResponse) {
            val context = itemView.context

            // Usamos los campos de StudentResponse
            binding.tvUserName.text = student.fullName
            binding.tvUserEmail.text = student.username

            val avatarUrl = if (!student.profileImageUrl.isNullOrBlank()) {
                student.profileImageUrl
            } else {
                "https://api.dicebear.com/8.x/adventurer/svg?seed=${student.username}"
            }
            Glide.with(context)
                .load(avatarUrl)
                .placeholder(R.drawable.ic_account_circle_24)
                .into(binding.ivUserAvatar)

            binding.chipUserRole.text = "ESTUDIANTE"
            binding.chipUserRole.setChipBackgroundColorResource(R.color.secondary)

            // Menú de opciones
            binding.btnMoreOptions.setOnClickListener { view ->
                val popup = PopupMenu(context, view)
                popup.menuInflater.inflate(R.menu.admin_user_item_menu, popup.menu)
                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_edit_user -> onOptionClick(student, "edit")
                        R.id.action_delete_user -> onOptionClick(student, "delete")
                    }
                    true
                }
                popup.show()
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