package com.nvc.orderfood.data.source.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.nvc.orderfood.model.Notification
import com.nvc.orderfood.model.Order
import com.nvc.orderfood.utils.MySharePre
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow

class NotificationRemote(
    private val userNotificationRef: DatabaseReference,
    private val orderRef : DatabaseReference,
    private val mySharePre: MySharePre
) {
    fun getDataRemote() = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val items = snapshot.children.map { ds ->
                    ds.getValue(Notification::class.java)
                }
                trySendBlocking(items.reversed().filterNotNull())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        userNotificationRef.child(mySharePre.getUser()!!.id)
            .addValueEventListener(listener)
        awaitClose {
            userNotificationRef.child(mySharePre.getUser()!!.id).removeEventListener(listener)
        }
    }

    fun getOrder(orderID : String) = callbackFlow {
        val listener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                trySendBlocking(snapshot.getValue(Order::class.java))
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        orderRef.child(orderID).addListenerForSingleValueEvent(listener)
        awaitClose {
            orderRef.child(orderID).removeEventListener(listener)
        }
    }
}