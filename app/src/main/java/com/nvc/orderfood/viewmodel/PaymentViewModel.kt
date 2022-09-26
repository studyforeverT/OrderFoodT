package com.nvc.orderfood.viewmodel

import androidx.lifecycle.*
import com.nvc.orderfood.data.database.cart.CartDao
import com.nvc.orderfood.model.Order
import com.nvc.orderfood.model.User
import com.nvc.orderfood.repository.OrderRepository
import com.nvc.orderfood.utils.CheckNetwork
import com.nvc.orderfood.utils.Convert.Companion.toTimeStampString
import com.nvc.orderfood.utils.MySharePre
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val cartDao: CartDao,
    private val mySharePre: MySharePre,
    private val checkNetwork: CheckNetwork
) :
    ViewModel() {
    val listOrderItems = cartDao.getAllCartsLiveData()
    val totalPrice = cartDao.getTotal()
    private val _statusPay = MutableLiveData(false)
    private val _userInfo = MutableLiveData<User>(mySharePre.getUser())
    val userInfo: LiveData<User>
        get() = _userInfo
    private val _orderedDish = MutableLiveData<Order?>(null)
    val orderedDish: LiveData<Order?>
        get() = _orderedDish

    fun pay() {
        if(checkNetwork.isNetworkAvailable()) {
            _statusPay.value = false
            val order = userInfo.value?.let {
                Order(
                    userID = it.id,
                    address = userInfo.value!!.addressUser!!,
                    totalPrice = totalPrice.value!!,
                    status = 2,
                    timestamp = toTimeStampString(),
                    phone = userInfo.value!!.phoneUser!!,
                    receiverName = userInfo.value!!.nameUser!!
                )
            }
            viewModelScope.launch(Dispatchers.IO) {
                if (order != null) {
                    orderRepository.pay(order, listOrderItems.value!!).collect {
                        when {
                            it.isSuccess -> {
                                withContext(Dispatchers.Main) {
                                    _orderedDish.value = it.getOrNull()
                                    cartDao.deleteAllCarts()
                                    _orderedDish.value = null
                                }
                            }
                            else -> {
                                withContext(Dispatchers.Main) {
                                    _statusPay.value = false
                                }
                            }
                        }
                    }
                }
            }
        }else{
            checkNetwork.showToastNetworkError()
        }
    }

    fun changeUserInfo(name: String, phone: String, address: String) {
        if(checkNetwork.isNetworkAvailable()) {
            _userInfo.value = userInfo.value?.apply {
                nameUser = name
                phoneUser = phone
                addressUser = address
            }
        }else{
            checkNetwork.showToastNetworkError()
        }
    }
    fun resetUserInfo(){
        _userInfo.value = mySharePre.getUser()
    }
}