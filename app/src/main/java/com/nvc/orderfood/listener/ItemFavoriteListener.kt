package com.nvc.orderfood.listener

import com.nvc.orderfood.model.Favorite

interface ItemFavoriteListener {
    fun onClickItemListener(favorite: Favorite)
    fun onClickRatingListener(rating: Float)
    fun onClickFavoriteListener(favorite: Favorite)
}