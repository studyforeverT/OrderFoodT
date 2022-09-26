package com.nvc.orderfood.repository

import androidx.lifecycle.LiveData
import com.nvc.orderfood.data.source.ICategorySourceService
import com.nvc.orderfood.data.source.firebase.CategoryRemote
import com.nvc.orderfood.data.source.local.CategoryLocal
import com.nvc.orderfood.model.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryRepository(
    private val categoryRemote: CategoryRemote,
    private val categoryLocal: CategoryLocal
) : ICategorySourceService {

    override fun getListCategoryLiveData(): LiveData<List<Category>> {
        return categoryLocal.getListCategoryLiveData()
    }

    override fun getDataRemote() {
        CoroutineScope(Dispatchers.IO).launch {
            categoryRemote.getDataRemote().collect {
                when {
                    it.isSuccess -> {
                        val list = it.getOrNull()
                        if (list != null && list != categoryLocal.getListCategory()) {
                            categoryLocal.updateDataLocal(list)
                        }
                    }
                    it.isFailure -> {
                        it.exceptionOrNull()?.printStackTrace()

                    }
                }
            }
        }
    }
}