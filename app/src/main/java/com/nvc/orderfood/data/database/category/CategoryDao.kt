package com.nvc.orderfood.data.database.category

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nvc.orderfood.model.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(ctg: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertListCategory(list: List<Category>)

    @Update
    suspend fun updateCategory(ctg: Category)

    @Delete
    suspend fun deleteCategory(ctg: Category)

    @Query("DELETE FROM tb_category")
    suspend fun deleteAllCategory()

    @Query("SELECT * FROM tb_category")
    fun getAllCategoryLiveData(): LiveData<List<Category>>

    @Query("SELECT * FROM tb_category")
    fun getAllCategory(): List<Category>

}