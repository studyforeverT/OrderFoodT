package com.nvc.orderfood.data.source.local

import androidx.lifecycle.LiveData
import com.nvc.orderfood.data.database.favorite.FavoriteDao
import com.nvc.orderfood.model.Favorite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteLocal(private val favoriteDao: FavoriteDao) {
    fun insertFavoriteLocal(favorite: Favorite) {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteDao.insertFavorite(favorite)
        }
    }

    fun deleteFavoriteLocal(favorite: Favorite) {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteDao.deleteFavorite(favorite)
        }
    }

    fun getListFavoriteLiveData(): LiveData<List<Favorite>> {
        return favoriteDao.getAllFavoriteLiveData()
    }

    fun updateFavoriteLocal(list: List<Favorite>) {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteDao.deleteAllFavorite()
            favoriteDao.insertListFavorite(list)
        }
    }
}