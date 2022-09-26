package com.nvc.orderfood.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.nvc.orderfood.model.Food
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow


class SearchRepository (private val foodRef : DatabaseReference) {

    fun getByName(name: String) = callbackFlow<List<Food>> {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = ArrayList<Food>()
                for (ds in snapshot.children) {
                    if (ds.child("name").value.toString().lowercase().contains(name.lowercase())) {
                        val food = ds.getValue(Food::class.java)?.apply {
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
                        if (food != null) {
                            items.add(food)
                        }
                    }
                }
                Log.d("thien", items.toString())
                trySendBlocking(items)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        foodRef.addListenerForSingleValueEvent(listener)

//        orderByChild("name").startAt(name).endAt("$name\uf8ff" )

            awaitClose { foodRef.removeEventListener(listener) }
    }

}