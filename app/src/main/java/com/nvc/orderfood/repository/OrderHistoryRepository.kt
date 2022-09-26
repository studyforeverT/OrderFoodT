package com.nvc.orderfood.repository

import androidx.lifecycle.LiveData
import com.nvc.orderfood.data.source.firebase.OrderHistoryRemote
import com.nvc.orderfood.data.source.local.OrderLocal
import com.nvc.orderfood.model.Order
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class OrderHistoryRepository(private val orderHistoryRemote: OrderHistoryRemote, private val orderLocal: OrderLocal) {

    fun getDataRemote(): Flow<Result<List<Order>>> {
        CoroutineScope(Dispatchers.IO).launch {
            orderHistoryRemote.getDataRemote().collect {
                when {
                    it.isSuccess -> {
                        val list = it.getOrNull()
                        orderLocal.updateOrderLocal(list!!)

                    }
                    it.isFailure -> {
                        it.exceptionOrNull()?.printStackTrace()

                    }
                }
            }
        }
        return orderHistoryRemote.getDataRemote()
    }

    fun getListOrderLiveData(): LiveData<List<Order>> {
        return orderLocal.getListOrderLiveData()
    }
}