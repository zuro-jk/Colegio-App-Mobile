package com.jeyix.schooljeyix.ui.admin.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.databinding.ItemRecentActivityBinding

class RecentActivityAdapter : ListAdapter<ActivityItem, RecentActivityAdapter.ActivityViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ItemRecentActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ActivityViewHolder(private val binding: ItemRecentActivityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ActivityItem) {
            binding.tvActivityTitle.text = item.title
            binding.tvActivitySubtitle.text = item.subtitle
            binding.tvActivityTime.text = item.timestamp

            val iconRes = when(item.type) {
                ActivityType.NEW_STUDENT -> R.drawable.ic_student_24
                ActivityType.PAYMENT -> R.drawable.ic_payments_24
                ActivityType.ALERT -> R.drawable.ic_notifications_24
            }
            binding.imgActivityIcon.setImageResource(iconRes)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ActivityItem>() {
        override fun areItemsTheSame(oldItem: ActivityItem, newItem: ActivityItem) = oldItem.title == newItem.title // Idealmente usar ID único
        override fun areContentsTheSame(oldItem: ActivityItem, newItem: ActivityItem) = oldItem == newItem
    }
}