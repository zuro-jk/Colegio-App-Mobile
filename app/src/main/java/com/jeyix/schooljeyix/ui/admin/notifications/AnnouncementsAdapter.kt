package com.jeyix.schooljeyix.ui.admin.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.databinding.ItemAnnouncementAdminBinding
import com.jeyix.schooljeyix.domain.model.Announcement
import com.jeyix.schooljeyix.domain.model.AnnouncementType

class AnnouncementsAdapter(
    private val onClick: (Announcement) -> Unit
) : ListAdapter<Announcement, AnnouncementsAdapter.AnnouncementViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val binding = ItemAnnouncementAdminBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AnnouncementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AnnouncementViewHolder(private val binding: ItemAnnouncementAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Announcement) {
            val context = itemView.context

            binding.tvAnnouncementTitle.text = item.title
            binding.tvAnnouncementBody.text = item.body
            binding.tvNotificationTime.text = item.timestamp
            binding.unreadIndicator.isVisible = item.isUnread
            val (backgroundRes, iconRes, iconTint) = when (item.type) {
                AnnouncementType.URGENT -> Triple(
                    R.drawable.bg_notification_urgent,
                    R.drawable.ic_error_24,
                    R.color.error
                )

                AnnouncementType.PAYMENT -> Triple(
                    R.drawable.bg_notification_info,
                    R.drawable.ic_payments_24,
                    R.color.success
                )

                else -> Triple(
                    R.drawable.bg_notification_info,
                    R.drawable.ic_campaign_24,
                    R.color.primary
                )
            }

            binding.container.setBackgroundResource(backgroundRes)
            binding.ivNotificationIcon.setImageResource(iconRes)
            binding.ivNotificationIcon.setColorFilter(
                ContextCompat.getColor(context, iconTint)
            )

            itemView.setOnClickListener { onClick(item) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Announcement>() {
        override fun areItemsTheSame(oldItem: Announcement, newItem: Announcement) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Announcement, newItem: Announcement) =
            oldItem == newItem
    }
}