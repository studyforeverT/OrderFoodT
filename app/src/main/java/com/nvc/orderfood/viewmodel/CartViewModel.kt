package com.nvc.orderfood.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nvc.orderfood.model.Cart
import com.nvc.orderfood.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    val listCart = cartRepository.getListCartLiveData()
    private val _message = MutableLiveData<String?>(null)
    val message: LiveData<String?>
        get() = _message

    fun resetMessage(){
        _message.value = null
    }

    fun getDataRemote() {
        cartRepository.getDataRemote()
    }

    val total = cartRepository.getTotal()


    fun plusCart(cart: Cart) {
        viewModelScope.launch {
            cartRepository.plusCart(cart).collect {
                _message.value = it
            }
        }

    }

    fun minusCart(cart: Cart) {
        cartRepository.minusCart(cart)
    }

    fun insertCart(cart: Cart) {
        viewModelScope.launch {
            cartRepository.insertCard(cart).collect {
                _message.value = it
            }
        }
    }

    fun deleteCart(cart: Cart) {
        viewModelScope.launch {
            cartRepository.deleteCardRemote(cart).collect {
                _message.value = it
            }
        }
    }

    fun addToCart(newCart: Cart) {
        listCart.value!!.find {
            it.id == newCart.id
        }.apply {
            if (this != null) {
                plusCart(this)
            } else {
                newCart.quantity = 1
                insertCart(newCart)
            }
        }
    }


}
