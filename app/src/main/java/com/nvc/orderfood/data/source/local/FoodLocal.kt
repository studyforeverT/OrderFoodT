package com.nvc.orderfood.data.source.local

import androidx.lifecycle.LiveData
import com.nvc.orderfood.data.database.food.FoodDao
import com.nvc.orderfood.data.source.IFoodSourceService
import com.nvc.orderfood.model.Food
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class FoodLocal(private val foodDao: FoodDao) : IFoodSourceService {
    override fun insertListFood(list: List<Food>) {
        CoroutineScope(Dispatchers.IO).launch {
            foodDao.insertListFood(list)
        }
    }

    override fun insertFood(food: Food) {
        CoroutineScope(Dispatchers.IO).launch {
            foodDao.insertFood(food)
        }
    }

    override fun updateFood(food: Food) {
        CoroutineScope(Dispatchers.IO).launch {
            foodDao.updateFood(food)
        }
    }

    override fun deleteFood(food: Food) {
        CoroutineScope(Dispatchers.IO).launch {
            foodDao.deleteFood(food)
        }
    }

    override fun deleteAllFood() {
        CoroutineScope(Dispatchers.IO).launch {
            foodDao.deleteAllFood()
        }
    }

    override fun getListFoodLiveData(): LiveData<List<Food>> {
            return foodDao.getAllFoodLiveData()
    }

    override fun getDataRemote(): Flow<Result<List<Food>>> {
        TODO("Not yet implemented")
    }
    fun updateDataLocal(list : List<Food>){
        CoroutineScope(Dispatchers.IO).launch {
            foodDao.deleteAllFood()
            foodDao.insertListFood(list)
        }
    }

    fun getAllFoodLocal(): List<Food>{
        return foodDao.getAllFood()
    }

}