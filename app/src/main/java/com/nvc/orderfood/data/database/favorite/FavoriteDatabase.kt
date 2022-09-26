package com.nvc.orderfood.data.database.favorite

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nvc.orderfood.model.Favorite

@Database(entities = [Favorite::class], version = 1, exportSchema = false)
abstract class FavoriteDatabase: RoomDatabase() {
    abstract fun favoriteDao() : FavoriteDao
}
