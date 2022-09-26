package com.nvc.orderfood.repository

import androidx.lifecycle.LiveData
import com.nvc.orderfood.data.source.firebase.CartRemote
import com.nvc.orderfood.data.source.local.CartLocal
import com.nvc.orderfood.model.Cart
import com.nvc.orderfood.utils.CheckNetwork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class CartRepository(
    private val checkNetwork: CheckNetwork,
    private val cartRemote: CartRemote,
    private val cartLocal: CartLocal
) {
    fun insertCard(cart: Cart) = callbackFlow<String?> {
        cartRemote.insertCard(cart).apply {
            if (checkNetwork.isNetworkAvailable()) {
                CoroutineScope(Dispatchers.IO).launch {
                    collect {
                        cartLocal.insertCard(cart)
                        trySendBlocking("Add cart successfully")
                    }
                }
            } else {
                checkNetwork.showToastNetworkError()
            }
        }
        awaitClose {

        }
    }


    fun deleteCardRemote(cart: Cart) = callbackFlow<String> {
        cartRemote.deleteCardRemote(cart).apply {
            CoroutineScope(Dispatchers.IO).launch {
                collect {
                    cartLocal.deleteCardLocal(cart)
                    trySendBlocking("Delete cart successfully")
                }
            }
        }
        awaitClose { }
    }


    fun getListCartLiveData(): LiveData<List<Cart>> {
        return cartLocal.getListCartLiveData()
    }

    fun getDataRemote(): Flow<Result<List<Cart>>> {
        CoroutineScope(Dispatchers.IO).launch {
            cartRemote.getDataRemote().collect {
                when {
                    it.isSuccess -> {
                        val list = it.getOrNull()
                        if (list == null) {
                            cartLocal.deleteAllCardLocal()
                        } else {
                            cartLocal.updateCartLocal(list)
                        }

                    }
                    it.isFailure -> {
                        it.exceptionOrNull()?.printStackTrace()

                    }
                }
            }
        }
        return cartRemote.getDataRemote()
    }


    @OptIn(FlowPreview::class)
    fun plusCart(cart: Cart) = callbackFlow<String> {
        if (checkNetwork.isNetworkAvailable()) {
            CoroutineScope(Dispatchers.IO).launch {
                cart.quantity++
                cartLocal.updateCard(cart = cart)
                cartRemote.updateCart(cart).debounce(200L).collect {
                    when {
                        it.isSuccess -> {
                            trySendBlocking("The product has been added to cart")
                        }
                        it.isFailure -> {
                            checkNetwork.showToastNetworkError()
                        }
                    }

                }
            }
        } else {
            checkNetwork.showToastNetworkError()
        }
        awaitClose { }
    }

    @OptIn(FlowPreview::class)
    fun minusCart(cart: Cart) {
        if (checkNetwork.isNetworkAvailable()) {
            cart.quantity--
            CoroutineScope(Dispatchers.IO).launch {
                cartLocal.updateCard(cart = cart)
                cartRemote.updateCart(cart).debounce(200L).collect {
                    when {
                        it.isSuccess -> {
                        }
                        it.isFailure -> {
                            checkNetwork.showToastNetworkError()
                        }
                    }

                }
            }
        } else {
            checkNetwork.showToastNetworkError()
        }

    }

    fun getTotal(): LiveData<Int> {
        return cartLocal.getTotal()
    }
}