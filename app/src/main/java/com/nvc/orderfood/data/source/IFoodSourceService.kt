package com.nvc.orderfood.data.source

import androidx.lifecycle.LiveData
import com.nvc.orderfood.model.Food
import kotlinx.coroutines.flow.Flow

interface IFoodSourceService {
    fun insertListFood(list : List<Food>)
    fun insertFood(food : Food)
    fun updateFood(food : Food)
    fun deleteFood(food : Food)
    fun deleteAllFood()
    fun getListFoodLiveData() : LiveData<List<Food>>
    fun getDataRemote() : Flow<Result<List<Food>>>
}