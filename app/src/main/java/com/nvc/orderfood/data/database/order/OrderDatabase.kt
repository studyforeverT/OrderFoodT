package com.nvc.orderfood.data.database.order

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nvc.orderfood.model.Order

@Database(entities = [Order::class], version = 1, exportSchema = false)
abstract class OrderDatabase : RoomDatabase() {
    abstract fun orderDao() : OrderDao
}