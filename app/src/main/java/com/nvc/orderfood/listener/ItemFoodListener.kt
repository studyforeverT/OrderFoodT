package com.nvc.orderfood.listener

import com.nvc.orderfood.model.Food

interface ItemFoodListener {
    fun onClickFoodListener(food: Food)
    fun onClickAddToCart(food: Food)
}