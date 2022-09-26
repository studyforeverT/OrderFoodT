package com.nvc.orderfood.repository

import com.nvc.orderfood.data.source.firebase.OrderRemote
import com.nvc.orderfood.model.Cart
import com.nvc.orderfood.model.Order
import com.nvc.orderfood.model.OrderItem
import kotlinx.coroutines.flow.Flow

class OrderRepository(private val orderRemote : OrderRemote) {
    fun pay(order : Order, listOrderItems : List<Cart>) : Flow<Result<Order>> {
        return orderRemote.pay(order,listOrderItems)
    }
    fun getListOrderItems(orderID : String):Flow<Result<List<OrderItem>>>{
        return orderRemote.getListOrderItems(orderID)
    }

    fun rateFood(orderItem: OrderItem,rate : Int) : Flow<Int>{
        return orderRemote.rateFood(orderItem,rate)
    }
}