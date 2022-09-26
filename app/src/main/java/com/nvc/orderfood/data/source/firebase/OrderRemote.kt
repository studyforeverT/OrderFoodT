package com.nvc.orderfood.data.source.firebase

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.nvc.orderfood.model.Cart
import com.nvc.orderfood.model.Order
import com.nvc.orderfood.model.OrderItem
import com.nvc.orderfood.utils.MySharePre
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class OrderRemote(
    private val ordersRef: DatabaseReference,
    private val cartRef: DatabaseReference,
    private val foodRef: DatabaseReference,
    private val mySharePre: MySharePre
) {
    fun pay(order: Order, listOrderItems: List<Cart>) = callbackFlow<Result<Order>> {
        val taskSuccess = OnSuccessListener<Void> {
            mySharePre.getUser()?.let { it1 ->
                cartRef.child(it1.id).setValue(null)
                trySendBlocking(Result.success(order))
            }
        }
        val taskFailure = OnFailureListener {
            trySendBlocking(Result.failure(Throwable("Error when add to database")))
        }

        ordersRef.push().apply {
            CoroutineScope(Dispatchers.IO).launch {
                val list = ArrayList<OrderItem>()
                order.id = key!!
                listOrderItems.forEach { cart ->
                    list.add(
                        OrderItem(
                            id = cart.id,
                            orderID = key!!,
                            foodID = cart.id,
                            name = cart.name,
                            image = cart.image,
                            price = cart.price,
                            quantity = cart.quantity,
                            rate = 0
                        )
                    )
                }
                setValue(order).addOnSuccessListener(taskSuccess)
                    .addOnFailureListener(taskFailure)
                child("order_items").setValue(list)
            }
        }
        awaitClose { }


    }

    fun getListOrderItems(orderID: String) = callbackFlow<Result<List<OrderItem>>> {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.map { ds ->
                    ds.getValue(OrderItem::class.java)
                }
                trySendBlocking(Result.success(items.filterNotNull()))
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }
        }
        ordersRef.child(orderID).child("order_items").addListenerForSingleValueEvent(listener)
        awaitClose {
            ordersRef.child(orderID).child("order_items").removeEventListener(listener)
        }
    }

    fun rateFood(orderItem: OrderItem, rate: Int)= callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { ds ->
                    if(ds.child("id").value==orderItem.id){
                        ds.child("rate").ref.setValue(rate)
                    }
                }
                foodRef.child(orderItem.id).child("rate")
                    .child(mySharePre.getUser()!!.id).setValue(rate)
                trySendBlocking(rate)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        ordersRef.child(orderItem.orderID).child("order_items").addListenerForSingleValueEvent(listener)

        awaitClose {
            ordersRef.child(orderItem.orderID).child("order_items").removeEventListener(listener)
        }
    }
}