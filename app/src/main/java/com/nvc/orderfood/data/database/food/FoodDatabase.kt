package com.nvc.orderfood.data.database.food

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nvc.orderfood.model.Food


@Database(entities = [Food::class], version = 1, exportSchema = false)
abstract class FoodDatabase : RoomDatabase() {
    abstract fun foodDao() : FoodDao
}
