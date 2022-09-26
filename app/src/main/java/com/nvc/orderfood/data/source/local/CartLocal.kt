package com.nvc.orderfood.data.source.local

import androidx.lifecycle.LiveData
import com.nvc.orderfood.data.database.cart.CartDao
import com.nvc.orderfood.model.Cart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartLocal(private val cartDao: CartDao) {

    fun insertCard(cart: Cart) {
        CoroutineScope(Dispatchers.IO).launch {
            cartDao.insertCart(cart)
        }
    }

    fun updateCard(cart: Cart) {
        CoroutineScope(Dispatchers.IO).launch {
            cartDao.updateCart(cart)
        }
    }

    fun deleteCardLocal(cart: Cart) {
        CoroutineScope(Dispatchers.IO).launch {
            cartDao.deleteCart(cart)
        }
    }

    fun deleteAllCardLocal(){
        CoroutineScope(Dispatchers.IO).launch {
            cartDao.deleteAllCarts()
        }
    }


    fun getListCartLiveData(): LiveData<List<Cart>> {
        return cartDao.getAllCartsLiveData()
    }


    fun updateCartLocal(list: List<Cart>) {
        CoroutineScope(Dispatchers.IO).launch {
            cartDao.deleteAllCarts()
            cartDao.insertListCart(list)
        }
    }

    fun getTotal(): LiveData<Int> {
        return cartDao.getTotal()
    }
}