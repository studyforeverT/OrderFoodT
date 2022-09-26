package com.nvc.orderfood.data.source.firebase

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.nvc.orderfood.data.source.IProfileSourceService
import com.nvc.orderfood.model.User
import com.nvc.orderfood.utils.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.util.*


class ProfileRemote(
    private var profileRef: DatabaseReference,
    private var profileStorageRef: StorageReference,
    private val auth: FirebaseAuth
) :
    IProfileSourceService {

    override fun getProfile() = callbackFlow<Result<User>> {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                this@callbackFlow.trySendBlocking(Result.success(user!!))
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(
                    Result.failure(error.toException())
                )
            }
        }
        profileRef.child(auth.uid.toString()).addValueEventListener(postListener)

        awaitClose {
            profileRef.child(auth.uid.toString()).removeEventListener(postListener)
        }
    }

    override fun updateProfileImage(uri: Uri, user: User) = callbackFlow<Result<Boolean>> {
        CoroutineScope(Dispatchers.IO).launch {
            updateAvaterUser(uri).collect {
                user.imageUser = it
                updateInfo(user).collect { rs ->
                    if (rs.isSuccess) {
                        trySendBlocking(Result.success(true))
                    } else {
                        trySendBlocking(Result.failure(Throwable("Error")))
                    }
                }
            }
        }
        awaitClose { }

    }

    override fun updateInfo(user: User) = callbackFlow<Result<Boolean>> {

        val taskSuccess = OnSuccessListener<Void> {
            Log.d("TAGGGG", "onsuccess info")
            trySendBlocking(Result.success(true))
        }
        val taskFailure = OnFailureListener {
            Log.d("TAGGGG", "on fail info")
            trySendBlocking(Result.failure(Throwable("Error")))
        }

        profileRef.child(auth.currentUser?.uid.toString()).setValue(user)
            .addOnSuccessListener(taskSuccess)
            .addOnFailureListener(taskFailure)
        awaitClose { }
    }

    private fun updateAvaterUser(uri: Uri) = callbackFlow {
        val imageFolder: StorageReference =
            profileStorageRef.child(UUID.randomUUID().toString())
        imageFolder.putFile(uri)
            .addOnSuccessListener {
                imageFolder.downloadUrl.addOnSuccessListener { p0 ->
                    trySendBlocking(p0.toString())
                }.addOnFailureListener { e ->
                    e.printStackTrace()
                }
            }
        awaitClose(
        )
    }

    override fun logout(result: (UiState<String>) -> Unit) {
        auth.signOut()
    }

}