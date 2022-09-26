package com.nvc.orderfood.diff

import androidx.recyclerview.widget.DiffUtil
import com.nvc.orderfood.model.Cart
import com.nvc.orderfood.model.Notification

class NotificationDiff (
    private val oldNotification :List<Notification>,
    private val newNotification: List<Notification>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldNotification.size

    override fun getNewListSize() = newNotification.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNotification[oldItemPosition].id == newNotification[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNotification === newNotification
    }
}