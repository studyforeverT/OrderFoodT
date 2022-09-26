package com.nvc.orderfood.data.source.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.nvc.orderfood.data.source.IFoodSourceService
import com.nvc.orderfood.model.Food
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow

class FoodRemote(private val foodRef: DatabaseReference) : IFoodSourceService {
    override fun insertListFood(list: List<Food>) {
        TODO("Not yet implemented")
    }

    override fun insertFood(food: Food) {
        TODO("Not yet implemented")
    }

    override fun updateFood(food: Food) {
        TODO("Not yet implemented")
    }

    override fun deleteFood(food: Food) {
        TODO("Not yet implemented")
    }

    override fun deleteAllFood() {
        TODO("Not yet implemented")
    }

    override fun getListFoodLiveData(): LiveData<List<Food>> {
        TODO("Not yet implemented")
    }

    override fun getDataRemote() = callbackFlow<Result<List<Food>>> {
        val foodListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items = dataSnapshot.children.map { ds ->
                    ds.getValue(Food::class.java)?.apply {
                        var sum = 0f
                        if (ds.hasChild("rate")) {
                            for (s in ds.child("rate").children) {
                                sum += s.getValue(Float::class.java)!!
                            }
                        }
                        val rateCount = ds.child("rate").childrenCount
                        rating = if (rateCount != 0L) {
                            sum / rateCount
                        } else sum
                    }
                }

                this@callbackFlow.trySendBlocking(Result.success(items.filterNotNull()))
            }
        }
        foodRef.addListenerForSingleValueEvent(foodListener)
        awaitClose {
            foodRef.removeEventListener(foodListener)
        }
    }

    fun getListFoodByCategory(categoryID: String) = callbackFlow<Result<List<Food>>> {
        val foodListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items = dataSnapshot.children.map { ds ->
                    ds.getValue(Food::class.java)?.apply {
                        var sum = 0f
                        if (ds.hasChild("rate")) {
                            for (s in ds.child("rate").children) {
                                sum += s.getValue(Float::class.java)!!
                            }
                        }
                        val rateCount = ds.child("rate").childrenCount
                        rating = if (rateCount != 0L) {
                            sum / rateCount
                        } else sum
                    }
                }
                Log.d("TAGGG", items.size.toString())
                this@callbackFlow.trySendBlocking(Result.success(items.filterNotNull()))
            }
        }
        foodRef.orderByChild("categoryID").equalTo(categoryID)
            .addListenerForSingleValueEvent(foodListener)
        awaitClose {
            foodRef.orderByChild("categoryID").equalTo(categoryID).removeEventListener(foodListener)
        }
    }
}