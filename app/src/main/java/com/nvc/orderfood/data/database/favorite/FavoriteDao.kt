package com.nvc.orderfood.data.database.favorite

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nvc.orderfood.model.Favorite

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertListFavorite(list: List<Favorite>)

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

    @Query("SELECT * FROM tb_favorite")
    fun getAllFavoriteLiveData(): LiveData<List<Favorite>>

    @Query("DELETE FROM tb_favorite")
    suspend fun deleteAllFavorite()
}