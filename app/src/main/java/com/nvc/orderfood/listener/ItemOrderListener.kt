package com.nvc.orderfood.listener

import com.nvc.orderfood.model.Cart
import com.nvc.orderfood.model.Order
import com.nvc.orderfood.model.OrderItem

interface ItemOrderListener {
    fun onClickOrderListener(order: Order)
}