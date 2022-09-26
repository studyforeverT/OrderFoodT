package com.nvc.orderfood.data.source.local

import androidx.lifecycle.LiveData
import com.nvc.orderfood.data.database.category.CategoryDao
import com.nvc.orderfood.model.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryLocal(private val categoryDao: CategoryDao) {
    fun getListCategoryLiveData() : LiveData<List<Category>>{
        return categoryDao.getAllCategoryLiveData()
    }
    fun updateDataLocal(list : List<Category>){
        CoroutineScope(Dispatchers.IO).launch {
            categoryDao.deleteAllCategory()
            categoryDao.insertListCategory(list)
        }
    }
    fun getListCategory() : List<Category>{
        return categoryDao.getAllCategory()
    }
}