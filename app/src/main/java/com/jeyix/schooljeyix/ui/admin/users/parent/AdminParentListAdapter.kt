package com.jeyix.schooljeyix.ui.admin.users.parent

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.data.remote.feature.parent.response.ParentResponse
import com.jeyix.schooljeyix.databinding.ItemUserAdminBinding

class AdminParentListAdapter(
    private val onOptionClick: (user: ParentResponse, action: String) -> Unit
) : ListAdapter<ParentResponse, AdminParentListAdapter.ParentViewHolder>(ParentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val binding = ItemUserAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ParentViewHolder(private val binding: ItemUserAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(parent: ParentResponse) {
            val context = itemView.context
            val user = parent.user

            if (user == null) {
                binding.tvUserName.text = "Sin usuario"
                binding.tvUserEmail.text = ""
                binding.ivUserAvatar.setImageResource(R.drawable.ic_account_circle_24)
                return
            }

            binding.tvUserName.text = user.fullName ?: "Sin nombre"
            binding.tvUserEmail.text = user.email ?: "Sin correo"

            val avatarUrl = if (!user.profileImageUrl.isNullOrBlank()) {
                user.profileImageUrl
            } else {
                "https://api.dicebear.com/8.x/adventurer/png?seed=${user.username}"
            }
            Glide.with(context)
                .load(avatarUrl)
                .placeholder(R.drawable.ic_account_circle_24)
                .error(R.drawable.ic_account_circle_24)
                .into(binding.ivUserAvatar)

            binding.chipUserRole.text = "PADRE"
            binding.chipUserRole.setChipBackgroundColorResource(R.color.primary)

            binding.btnMoreOptions.setOnClickListener { view ->
                val popup = PopupMenu(context, view)
                popup.menuInflater.inflate(R.menu.admin_user_item_menu, popup.menu)
                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_edit_user -> onOptionClick(parent, "edit")
                        R.id.action_delete_user -> onOptionClick(parent, "delete")
                    }
                    true
                }
                popup.show()
            }
        }
    }

    class ParentDiffCallback : DiffUtil.ItemCallback<ParentResponse>() {
        override fun areItemsTheSame(oldItem: ParentResponse, newItem: ParentResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ParentResponse, newItem: ParentResponse): Boolean {
            return oldItem == newItem
        }
    }
}