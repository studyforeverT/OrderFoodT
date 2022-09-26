package com.nvc.orderfood.listener

import com.nvc.orderfood.model.Category

interface ItemCategoryListener {
    fun onClickCategoryListener(category: Category)
}