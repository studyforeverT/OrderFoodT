package com.nvc.orderfood.repository

import com.nvc.orderfood.data.source.firebase.NotificationRemote
import com.nvc.orderfood.model.Notification
import com.nvc.orderfood.utils.CheckNetwork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationRepository(
    private val notificationRemote: NotificationRemote,
    private val checkNetwork: CheckNetwork
) {
    fun getDataRemote(): Flow<List<Notification>> {
        return notificationRemote.getDataRemote()
    }

    fun getOrder(orderID: String) = callbackFlow {
        CoroutineScope(Dispatchers.IO).launch{
            if (checkNetwork.isNetworkAvailable()) {
                notificationRemote.getOrder(orderID).collect {
                    trySendBlocking(it)
                }
            } else {
                withContext(Dispatchers.Main){
                    checkNetwork.showToastNetworkError()
                }

            }
        }

        awaitClose { }
    }
}