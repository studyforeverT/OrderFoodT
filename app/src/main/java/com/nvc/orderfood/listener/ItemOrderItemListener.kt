package com.nvc.orderfood.listener

import com.nvc.orderfood.model.Cart
import com.nvc.orderfood.model.OrderItem

interface ItemOrderItemListener {
    fun onClickFoodListener(orderItem: OrderItem)
    fun onRate(orderItem: OrderItem)
}