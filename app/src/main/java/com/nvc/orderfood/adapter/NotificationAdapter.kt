package com.nvc.orderfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nvc.orderfood.databinding.ItemNotificationBinding
import com.nvc.orderfood.diff.NotificationDiff
import com.nvc.orderfood.listener.ItemNotificationListener
import com.nvc.orderfood.model.Notification
import javax.inject.Inject

class NotificationAdapter @Inject constructor() : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    private val listNotification = arrayListOf<Notification>()
    private var mItemNotificationListener: ItemNotificationListener? = null

    fun setOnClickListener(onClickListener: ItemNotificationListener) {
        this.mItemNotificationListener = onClickListener
    }

    class ViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: Notification) {
            binding.notification = notification
            binding.executePendingBindings()
        }
    }

    fun submitData(temp: List<Notification>) {
        val diff = DiffUtil.calculateDiff(NotificationDiff(listNotification, temp))
        listNotification.clear()
        listNotification.addAll(temp)
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listNotification[position].let { notification ->
            holder.bind(notification)
            holder.itemView.setOnClickListener {
                mItemNotificationListener?.onClickItem(notification)
            }
        }
    }

    override fun getItemCount(): Int {
        return listNotification.size
    }

}