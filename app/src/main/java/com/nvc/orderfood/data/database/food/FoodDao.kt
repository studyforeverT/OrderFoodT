package com.nvc.orderfood.data.database.food

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nvc.orderfood.model.Food

@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food : Food)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertListFood(list : List<Food>)

    @Update
    suspend fun updateFood(food : Food)

    @Delete
    suspend fun deleteFood(food : Food)

    @Query("DELETE FROM tb_food")
    suspend fun deleteAllFood()

    @Query("SELECT * FROM tb_food")
    fun getAllFoodLiveData() : LiveData<List<Food>>

    @Query("SELECT * FROM tb_food")
    fun getAllFood() : List<Food>


}