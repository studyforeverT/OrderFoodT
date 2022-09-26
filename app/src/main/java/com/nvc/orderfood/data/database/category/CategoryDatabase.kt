package com.nvc.orderfood.data.database.category

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nvc.orderfood.model.Category


@Database(entities = [Category::class], version = 1, exportSchema = false)
abstract class CategoryDatabase : RoomDatabase() {
    abstract fun categoryDao() : CategoryDao
}
