package com.nvc.orderfood.data.source.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.nvc.orderfood.data.source.IOrderSourceService
import com.nvc.orderfood.model.Order
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow

class OrderHistoryRemote(
    private val orderHistoryRef: DatabaseReference,
    private val auth: FirebaseAuth
) : IOrderSourceService {

    override fun getDataRemote() = callbackFlow<Result<List<Order>>> {
        val orderHistoryListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orderHistory = snapshot.children.map { orderHisList ->
                    orderHisList.getValue(Order::class.java)
                }
                this@callbackFlow.trySendBlocking(Result.success(orderHistory.filterNotNull()))
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }
        }
        orderHistoryRef
            .orderByChild("userID")
            .equalTo(auth.uid)
            .addListenerForSingleValueEvent(orderHistoryListener)
        awaitClose {
            orderHistoryRef.removeEventListener(orderHistoryListener)
        }
    }
}