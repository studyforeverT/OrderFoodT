package com.nvc.orderfood.data.database.user

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nvc.orderfood.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase(){
    abstract fun userDao() : UserDao
}