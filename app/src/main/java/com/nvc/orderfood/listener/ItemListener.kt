package com.nvc.orderfood.listener

import com.nvc.orderfood.model.Cart
import com.nvc.orderfood.model.Food

interface ItemListener<T> {
    fun onClickItemListener(item: T)

    interface ItemFoodListener{
        fun onClickAddToCart(food: Food)
    }

    interface ItemCartListener{
        fun onPlus(cart: Cart)
        fun onMinus(cart: Cart)
    }
}