package com.nvc.orderfood.data.source.local

import com.nvc.orderfood.data.database.user.UserDao
import com.nvc.orderfood.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//room
class AuthLocal(private val userDao: UserDao) {

    fun registerInsertUser(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.insertUser(user)
        }
    }

}