package com.nvc.orderfood.data.database.cart

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nvc.orderfood.model.Cart

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: Cart)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertListCart(list : List<Cart>)

    @Update
    suspend fun updateCart(cart: Cart)

    @Delete
    suspend fun deleteCart(cart: Cart)

    @Query("DELETE FROM tb_cart")
    suspend fun deleteAllCarts()

    @Query("SELECT * FROM tb_cart")
    fun getAllCartsLiveData() : LiveData<List<Cart>>

    @Query("SELECT SUM(col_quantity*col_price) FROM tb_cart")
    fun getTotal(): LiveData<Int>
}