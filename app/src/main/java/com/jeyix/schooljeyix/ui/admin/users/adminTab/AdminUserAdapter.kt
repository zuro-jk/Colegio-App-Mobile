package com.jeyix.schooljeyix.ui.admin.users.adminTab

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.data.remote.feature.auth.response.UserProfileResponse
import com.jeyix.schooljeyix.databinding.ItemUserAdminBinding

class AdminUserAdapter(
    private val onOptionClick: (user: UserProfileResponse, action: String) -> Unit
) : ListAdapter<UserProfileResponse, AdminUserAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserAdminBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class UserViewHolder(private val binding: ItemUserAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserProfileResponse) {
            val context = itemView.context

            binding.tvUserName.text = user.fullName
            binding.tvUserEmail.text = user.email

            val avatarUrl = if (!user.profileImageUrl.isNullOrBlank()) {
                user.profileImageUrl
            } else {
                "https://api.dicebear.com/8.x/adventurer/svg?seed=${user.username}"
            }
            Glide.with(context)
                .load(avatarUrl)
                .placeholder(R.drawable.ic_account_circle_24)
                .error(R.drawable.ic_account_circle_24)
                .into(binding.ivUserAvatar)

            val primaryRole = user.roles?.firstOrNull() ?: "SIN ROL"
            binding.chipUserRole.text = primaryRole.replace("ROLE_", "")

            val chipColor = when (primaryRole) {
                "ROLE_ADMIN" -> R.color.error
                "ROLE_TEACHER" -> R.color.accent_teal
                "ROLE_PARENT" -> R.color.primary
                "ROLE_STUDENT" -> R.color.secondary
                else -> R.color.gray_medium
            }
            binding.chipUserRole.setChipBackgroundColorResource(chipColor)

            binding.btnMoreOptions.setOnClickListener { view ->
                val popup = PopupMenu(context, view)
                popup.menuInflater.inflate(R.menu.admin_user_item_menu, popup.menu)
                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_edit_user -> onOptionClick(user, "edit")
                        R.id.action_delete_user -> onOptionClick(user, "delete")
                    }
                    true
                }
                popup.show()
            }
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<UserProfileResponse>() {
        override fun areItemsTheSame(oldItem: UserProfileResponse, newItem: UserProfileResponse): Boolean {
            return oldItem.username == newItem.username
        }

        override fun areContentsTheSame(oldItem: UserProfileResponse, newItem: UserProfileResponse): Boolean {
            return oldItem == newItem
        }
    }
}