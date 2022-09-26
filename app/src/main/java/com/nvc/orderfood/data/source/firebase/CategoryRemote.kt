package com.nvc.orderfood.data.source.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.nvc.orderfood.model.Category
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow

class CategoryRemote(private val categoryRef: DatabaseReference) {
    fun getDataRemote() = callbackFlow<Result<List<Category>>> {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items =
                    dataSnapshot.children.map { ds ->
                        ds.getValue(Category::class.java)
                    }
                this@callbackFlow.trySendBlocking(Result.success(items.filterNotNull()))
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))

            }

        }
        categoryRef.addListenerForSingleValueEvent(listener)
        awaitClose { categoryRef.removeEventListener(listener) }
    }
}