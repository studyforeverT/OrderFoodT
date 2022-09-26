package com.nvc.orderfood.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nvc.orderfood.model.OrderItem
import com.nvc.orderfood.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(private val orderRepository: OrderRepository) :
    ViewModel() {
    private val _listOrderItems = MutableLiveData<List<OrderItem>>()
    val listOrderItems: LiveData<List<OrderItem>>
        get() = _listOrderItems

    fun getListOrderItems(orderID: String) {
        viewModelScope.launch {
            orderRepository.getListOrderItems(orderID).collect {
                when {
                    it.isSuccess ->
                        _listOrderItems.value = it.getOrNull()
                }
            }
        }
    }

    fun rateFood(orderItem: OrderItem, newRate: Int) {
        viewModelScope.launch {
            orderRepository.rateFood(orderItem, newRate).collect { rateResult ->
                _listOrderItems.value = listOrderItems.value?.apply {
                    find { it == orderItem }?.rate = rateResult
                }
            }
        }
    }
}
