package com.nvc.orderfood.repository

import androidx.lifecycle.LiveData
import com.nvc.orderfood.data.source.IFoodSourceService
import com.nvc.orderfood.data.source.firebase.FoodRemote
import com.nvc.orderfood.data.source.local.FoodLocal
import com.nvc.orderfood.model.Food
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class FoodRepository(private val foodRemote: FoodRemote, private val foodLocal: FoodLocal) :
    IFoodSourceService {
    override fun insertListFood(list: List<Food>) {
        TODO("Not yet implemented")
    }

    override fun insertFood(food: Food) {
        TODO("Not yet implemented")
    }

    override fun updateFood(food: Food) {
        TODO("Not yet implemented")
    }

    override fun deleteFood(food: Food) {
        TODO("Not yet implemented")
    }

    override fun deleteAllFood() {
        TODO("Not yet implemented")
    }

    override fun getListFoodLiveData(): LiveData<List<Food>> {
        return foodLocal.getListFoodLiveData()
    }

    override fun getDataRemote(): Flow<Result<List<Food>>> {
        CoroutineScope(Dispatchers.IO).launch {
            foodRemote.getDataRemote().collect {
                when {
                    it.isSuccess -> {
                        val list = it.getOrNull()
                        if (list != null && foodLocal.getAllFoodLocal() != list) {
                            foodLocal.updateDataLocal(list)
                        }
                    }
                    it.isFailure -> {
                        it.exceptionOrNull()?.printStackTrace()

                    }
                }
            }
        }

        return foodRemote.getDataRemote()

    }

    fun getDataRemoteByCategory(categoryID: String) {
        CoroutineScope(Dispatchers.IO).launch {
            foodRemote.getListFoodByCategory(categoryID).collect {
                when {
                    it.isSuccess -> {
                        val list = it.getOrNull()
                        foodLocal.updateDataLocal(list!!)
                    }
                    it.isFailure -> {
                        it.exceptionOrNull()?.printStackTrace()

                    }
                }
            }
        }
    }


}