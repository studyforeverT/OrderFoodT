package com.nvc.orderfood.viewmodel

import androidx.lifecycle.ViewModel
import com.nvc.orderfood.repository.OrderHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val orderHistoryRepository: OrderHistoryRepository
) : ViewModel() {
    val listOrder = orderHistoryRepository.getListOrderLiveData()

    fun getDataRemote() {
        orderHistoryRepository.getDataRemote()
    }
}