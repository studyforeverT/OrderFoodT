package com.nvc.orderfood.diff

import androidx.recyclerview.widget.DiffUtil
import com.nvc.orderfood.model.Cart

class CartDiff (
    private val oldCart: List<Cart>,
    private val newCart: List<Cart>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldCart.size

    override fun getNewListSize() = newCart.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCart[oldItemPosition].id == newCart[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCart === newCart
    }
}