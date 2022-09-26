package com.nvc.orderfood.data.source

import androidx.lifecycle.LiveData
import com.nvc.orderfood.model.Category

interface ICategorySourceService {
    fun getListCategoryLiveData() : LiveData<List<Category>>
    fun getDataRemote()
}