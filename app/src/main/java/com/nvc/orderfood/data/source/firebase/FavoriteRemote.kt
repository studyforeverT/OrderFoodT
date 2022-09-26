package com.nvc.orderfood.data.source.firebase

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.nvc.orderfood.data.source.IFavoriteSourceService
import com.nvc.orderfood.model.Favorite
import com.nvc.orderfood.utils.MySharePre
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow

class FavoriteRemote(
    private val favoriteRef: DatabaseReference,
    private val foodRef: DatabaseReference,
    private val mySharePre: MySharePre
) :
    IFavoriteSourceService {
    override fun getDataRemote() = callbackFlow<Result<List<Favorite>>> {
        val favoriteListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val foodItems = ArrayList<Favorite>()
                if (dataSnapshot.hasChildren()) {
                    var count = 0L
                    dataSnapshot.children.forEach { ds ->
                        count++
                        val listenerGetFood = object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                snapshot.child(ds.value.toString()).getValue(Favorite::class.java)
                                    ?.let {
                                        var sum = 0f
                                        if (snapshot.child(ds.value.toString()).hasChild("rate")) {
                                            for (s in snapshot.child(ds.value.toString())
                                                .child("rate").children) {
                                                sum += s.getValue(Float::class.java)!!
                                            }
                                        }
                                        val rateCount = snapshot.child(ds.value.toString())
                                            .child("rate").childrenCount
                                        it.rating = if (rateCount != 0L) {
                                            sum / rateCount
                                        } else sum
                                        foodItems.add(it)
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
                            .addListenerForSingleValueEvent(listenerGetFood)
                    }
                } else {
                    this@callbackFlow.trySendBlocking(Result.success(foodItems))
                }
            }
        }

        mySharePre.getUser()?.let {
            favoriteRef.child(it.id)
                .addListenerForSingleValueEvent(favoriteListener)
        }
        awaitClose {
            favoriteRef.child(mySharePre.getUser()!!.id).removeEventListener(favoriteListener)
        }

    }

    override fun deleteFavoriteRemote(favorite: Favorite) = callbackFlow {
        val taskSuccess = OnSuccessListener<Void> {
            trySendBlocking(true)
        }
        val taskFailure = OnFailureListener {
            trySendBlocking(false)
        }
        mySharePre.getUser()?.let {
            favoriteRef.child(it.id)
                .child(favorite.id)
                .removeValue()
                .addOnSuccessListener(taskSuccess)
                .addOnFailureListener(taskFailure)
        }
        awaitClose { }

    }

    override fun insertFavoriteRemote(favorite: Favorite) = callbackFlow<Boolean> {
        val taskSuccess = OnSuccessListener<Void> {
            trySendBlocking(true)
        }
        val taskFailure = OnFailureListener {
            trySendBlocking(false)
        }

        mySharePre.getUser()?.let {
            favoriteRef.child(it.id)
                .child(favorite.id)
                .setValue(favorite.id)
                .addOnSuccessListener(taskSuccess)
                .addOnFailureListener(taskFailure)
        }
        awaitClose { }
    }

}