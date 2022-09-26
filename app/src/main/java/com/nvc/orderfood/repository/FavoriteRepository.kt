package com.nvc.orderfood.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.nvc.orderfood.data.source.IFavoriteSourceService
import com.nvc.orderfood.data.source.firebase.FavoriteRemote
import com.nvc.orderfood.data.source.local.FavoriteLocal
import com.nvc.orderfood.model.Favorite
import com.nvc.orderfood.utils.CheckNetwork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FavoriteRepository(
    private val checkNetwork: CheckNetwork,
    private val favoriteRemote: FavoriteRemote,
    private val favoriteLocal: FavoriteLocal
) :
    IFavoriteSourceService {

    override fun getDataRemote(): Flow<Result<List<Favorite>>> {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteRemote.getDataRemote().collect {
                when {
                    it.isSuccess -> {
                        val list = it.getOrNull()
                        favoriteLocal.updateFavoriteLocal(list!!)
                    }
                    it.isFailure -> {
                        it.exceptionOrNull()?.printStackTrace()
                    }
                }
            }
        }
        return favoriteRemote.getDataRemote()
    }

    override fun deleteFavoriteRemote(favorite: Favorite): Flow<Boolean> {
        return favoriteRemote.deleteFavoriteRemote(favorite)
    }

    fun deleteFavoriteLocal(favorite: Favorite) {
        favoriteLocal.deleteFavoriteLocal(favorite)
    }

    fun checkNetwork() = checkNetwork.isNetworkAvailable()
    fun showToastNW() {
        checkNetwork.showToastNetworkError()
    }

    fun insertFavoriteLocal(favorite: Favorite) {
        favoriteLocal.insertFavoriteLocal(favorite)
    }

    override fun insertFavoriteRemote(favorite: Favorite): Flow<Boolean> {
        return favoriteRemote.insertFavoriteRemote(favorite)
    }

    fun getListFavoriteLiveData(): LiveData<List<Favorite>> {
        return favoriteLocal.getListFavoriteLiveData()
    }

}