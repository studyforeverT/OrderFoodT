package com.nvc.orderfood.data.source.local

import androidx.lifecycle.LiveData
import com.nvc.orderfood.data.database.order.OrderDao
import com.nvc.orderfood.model.Order
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderLocal(private val orderDao: OrderDao) {

    fun getListOrderLiveData(): LiveData<List<Order>> {
        return orderDao.getAllOrderLiveData()
    }

    fun updateOrderLocal(list: List<Order>) {
        CoroutineScope(Dispatchers.IO).launch {
            orderDao.deleteAllOrder()
            orderDao.insertListOrder(list)
        }
    }

}