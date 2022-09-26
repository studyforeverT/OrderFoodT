package com.nvc.orderfood.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nvc.orderfood.model.Notification
import com.nvc.orderfood.model.Order
import com.nvc.orderfood.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val notificationRepository: NotificationRepository
) :
    ViewModel() {
    private val _listNotifications = MutableLiveData<List<Notification>>()
    val listNotifications
    get() = _listNotifications


    fun getDataRemote(){
        viewModelScope.launch {
            notificationRepository.getDataRemote().collect{
                _listNotifications.value = it
            }
        }
    }

    fun getOrder(orderID : String): Flow<Order?> {
           return notificationRepository.getOrder(orderID)
    }
}