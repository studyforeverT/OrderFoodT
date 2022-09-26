package com.nvc.orderfood.data.source

import com.nvc.orderfood.model.Favorite
import kotlinx.coroutines.flow.Flow

interface IFavoriteSourceService {
    fun getDataRemote(): Flow<Result<List<Favorite>>>
    fun deleteFavoriteRemote(favorite: Favorite): Flow<Boolean>
    fun insertFavoriteRemote(favorite: Favorite): Flow<Boolean>
}