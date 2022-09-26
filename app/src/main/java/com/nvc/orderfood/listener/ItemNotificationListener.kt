package com.nvc.orderfood.listener

import com.nvc.orderfood.model.Notification
import com.nvc.orderfood.model.Order

interface ItemNotificationListener {
    fun onClickItem(notification: Notification)
}