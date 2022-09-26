package com.nvc.orderfood.data.source.firebase

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.nvc.orderfood.model.Cart
import com.nvc.orderfood.utils.MySharePre
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow


class CartRemote(
    private val cartRef: DatabaseReference,
    val foodRef: DatabaseReference,
    private val mySharePre: MySharePre
) {

    fun insertCard(cart: Cart) = callbackFlow<Result<Cart>> {
        val taskSuccess = OnSuccessListener<Void> {
            trySendBlocking(Result.success(cart))
        }
        val taskFailure = OnFailureListener {
            trySendBlocking(Result.failure(Throwable("Error")))
        }
        mySharePre.getUser()?.let {
            cartRef.child(it.id)
                .child(cart.id)
                .setValue(cart.quantity)
                .addOnSuccessListener(taskSuccess)
                .addOnFailureListener(taskFailure)
        }
        awaitClose {

        }
    }


    fun deleteCardRemote(cart: Cart) = callbackFlow<Result<Cart>> {
        val taskSuccess = OnSuccessListener<Void> {
            trySendBlocking(Result.success(cart))
        }
        val taskFailure = OnFailureListener {
            trySendBlocking(Result.failure(Throwable("Network Error")))
        }

        mySharePre.getUser()?.let {
            cartRef.child(it.id)
                .child(cart.id)
                .setValue(null)
                .addOnSuccessListener(taskSuccess)
                .addOnFailureListener(taskFailure)
        }
        awaitClose {
            //remove callback
        }
    }


    fun getDataRemote() = callbackFlow<Result<List<Cart>>> {
        val cartListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val foodItems = ArrayList<Cart>()
                if (dataSnapshot.hasChildren()) {
                    var count = 0L
                    dataSnapshot.children.map { ds ->
                        count++
                        val listenerFood = object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                snapshot.child(ds.key!!).getValue(Cart::class.java)
                                    ?.let {
                                        var sum = 0f
                                        if (snapshot.child(ds.key!!).hasChild("rate")) {
                                            for (s in snapshot.child(ds.key!!)
                                                .child("rate").children) {
                                                sum += s.getValue(Float::class.java)!!
                                            }
                                        }
                                        val rateCount =
                                            snapshot.child(ds.key!!).child("rate").childrenCount
                                        foodItems.add(it.apply {
                                            quantity = ds.value.toString().toInt()
                                            rating = if (rateCount != 0L) {
                                                sum / rateCount
                                            } else sum

                                        })
                                    }
                                if (dataSnapshot.childrenCount == count) {
                                    this@callbackFlow.trySendBlocking(Result.success(foodItems))
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        }
                        foodRef.orderByKey().equalTo(ds.key)
                            .addListenerForSingleValueEvent(listenerFood)
                    }
                } else {
                    this@callbackFlow.trySendBlocking(Result.success(foodItems))
                }
            }
        }
        mySharePre.getUser()?.let {
            cartRef.child(it.id)
                .addListenerForSingleValueEvent(cartListener)
        }
        awaitClose {
            cartRef.removeEventListener(cartListener)
        }
    }

    fun updateCart(cart: Cart) = callbackFlow<Result<Cart>> {
        val taskSuccess = OnSuccessListener<Void> {
            trySendBlocking(Result.success(cart))
        }
        val taskFailure = OnFailureListener {
            trySendBlocking(Result.failure(Throwable("Network Error")))
        }

        mySharePre.getUser()?.let {
            cartRef.child(it.id)
                .child(cart.id)
                .setValue(cart.quantity)
                .addOnSuccessListener(taskSuccess)
                .addOnFailureListener(taskFailure)
        }
        awaitClose {
            //remove callback
        }
    }


}