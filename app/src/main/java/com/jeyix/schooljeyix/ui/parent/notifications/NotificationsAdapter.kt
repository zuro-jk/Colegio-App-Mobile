package com.jeyix.schooljeyix.ui.parent.notifications

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.databinding.ItemNotificationBinding
import com.jeyix.schooljeyix.domain.model.Announcement
import com.jeyix.schooljeyix.domain.model.AnnouncementType

class NotificationsAdapter : ListAdapter<Announcement, NotificationsAdapter.NotificationViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NotificationViewHolder(private val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Announcement) {
            binding.tvNotificationTitle.text = item.title
            binding.tvNotificationBody.text = item.body
            binding.tvNotificationTime.text = item.timestamp

            binding.unreadIndicator.isVisible = item.isUnread

            val context = itemView.context
            when (item.type) {
                AnnouncementType.PAYMENT -> {
                    binding.ivNotificationIcon.setImageResource(R.drawable.ic_payments_24)
                    setIconColor(Color.parseColor("#4CAF50"))
                }
                AnnouncementType.URGENT -> {
                    binding.ivNotificationIcon.setImageResource(R.drawable.ic_error_24)
                    setIconColor(Color.parseColor("#F44336"))
                }
                AnnouncementType.INFO -> {
                    binding.ivNotificationIcon.setImageResource(R.drawable.ic_notifications_24)
                    setIconColor(ContextCompat.getColor(context, R.color.accent_purple))
                }
            }
        }

        private fun setIconColor(color: Int) {
            binding.ivNotificationIcon.imageTintList = ColorStateList.valueOf(color)
            binding.ivNotificationIcon.backgroundTintList = ColorStateList.valueOf(color)
            binding.ivNotificationIcon.background.alpha = 30
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Announcement>() {
        override fun areItemsTheSame(oldItem: Announcement, newItem: Announcement) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Announcement, newItem: Announcement) = oldItem == newItem
    }
}