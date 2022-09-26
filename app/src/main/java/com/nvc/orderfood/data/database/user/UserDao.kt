package com.nvc.orderfood.data.database.user

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nvc.orderfood.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM tb_user")
    suspend fun deleteAllUser()

    @Query("SELECT * FROM tb_user")
    fun getAllUserLiveData() : LiveData<List<User>>
}