package com.nvc.orderfood.listener

import com.nvc.orderfood.model.Cart
import com.nvc.orderfood.model.OrderItem

interface ItemCartListener {
    fun onClickFoodListener(cart: Cart)
    fun onPlus(cart: Cart)
    fun onMinus(cart: Cart)

}