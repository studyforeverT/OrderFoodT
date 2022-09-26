package com.nvc.orderfood.data.database.cart

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nvc.orderfood.model.Cart


@Database(entities = [Cart::class], version = 2, exportSchema = false)
abstract class CartDatabase : RoomDatabase() {
    abstract fun cartDao() : CartDao
}