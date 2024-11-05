package miu.sts.app.njangui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import miu.sts.app.njangui.R
import miu.sts.app.njangui.databinding.ItemNotificationBinding
import miu.sts.app.njangui.models.NotificationItem

class NotificationAdapter : ListAdapter<NotificationItem, NotificationAdapter.NotificationViewHolder>(
    DiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = getItem(position)
        holder.bind(notification)
    }

    class NotificationViewHolder(private val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: NotificationItem) {
            binding.notificationTitle.text = notification.title
            binding.notificationBody.text = notification.body
            binding.notificationIcon.setImageResource(R.drawable.ic_notification)
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<NotificationItem>() {
        override fun areItemsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
            return oldItem.title == newItem.title // Define your comparison logic here
        }

        override fun areContentsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
            return oldItem == newItem
        }
    }
}
