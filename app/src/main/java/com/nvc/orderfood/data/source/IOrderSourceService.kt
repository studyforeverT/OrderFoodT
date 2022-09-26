package com.nvc.orderfood.data.source

import com.nvc.orderfood.model.Cart
import com.nvc.orderfood.model.Order
import kotlinx.coroutines.flow.Flow

interface IOrderSourceService {
    fun getDataRemote(): Flow<Result<List<Order>>>
}