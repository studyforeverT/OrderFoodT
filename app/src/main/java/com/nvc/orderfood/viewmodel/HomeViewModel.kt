package com.nvc.orderfood.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nvc.orderfood.repository.CategoryRepository
import com.nvc.orderfood.repository.FoodRepository
import com.nvc.orderfood.utils.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val foodRepository: FoodRepository
) : ViewModel() {
    private val _categoryIdActive = MutableLiveData(Constant.CATEGORY_ALL_ID)
    val categoryIdActive: LiveData<String>
        get() = _categoryIdActive
    val listFood = foodRepository.getListFoodLiveData()
    val listCategory = categoryRepository.getListCategoryLiveData()
    fun getDataRemote() {
        categoryRepository.getDataRemote()
        getDataFoodByCategory(categoryIdActive.value!!)
    }

    fun setCategoryActive(categoryID: String) {
        _categoryIdActive.value = categoryID
    }

    fun getDataFoodByCategory(categoryID: String) {
        if (categoryID == Constant.CATEGORY_ALL_ID) {
            foodRepository.getDataRemote()
        } else {
            foodRepository.getDataRemoteByCategory(categoryID)
        }

    }

}