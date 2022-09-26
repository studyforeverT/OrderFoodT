package com.nvc.orderfood.diff

import androidx.recyclerview.widget.DiffUtil
import com.nvc.orderfood.model.Favorite

class FavoriteDiff(
    private val oldFavorite: List<Favorite>,
    private val newFavorite: List<Favorite>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldFavorite.size

    override fun getNewListSize() = newFavorite.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavorite[oldItemPosition].id == newFavorite[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavorite === newFavorite
    }
}