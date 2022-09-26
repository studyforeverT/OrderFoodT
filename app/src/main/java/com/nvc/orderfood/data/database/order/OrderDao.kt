package com.nvc.orderfood.data.database.order

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nvc.orderfood.model.Order

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertListOrder(list : List<Order>)

    @Update
    suspend fun updateOrder(order: Order)

    @Delete
    suspend fun deleteOrder(order: Order)

    @Query("DELETE FROM tb_order")
    suspend fun deleteAllOrder()

    @Query("SELECT * FROM tb_order")
    fun getAllOrderLiveData() : LiveData<List<Order>>

}