package com.nvc.orderfood.utils

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast

class CheckNetwork(private val ctx: Context) {

    fun isNetworkAvailable(): Boolean {
        try {
            val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

//    fun check() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val connectedRef = Firebase.database.getReference(".info/connected")
//            connectedRef.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    Log.d("TAGGG", "ONNNN")
//                    val connected = snapshot.getValue(Boolean::class.java) ?: false
//                    _isConnected.value = connected
//                    Log.d("TAGGG", "ONNNN $connected")
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Log.d("TAGGG", "OFFFF")
//                    _isConnected.value = false
//                }
//            })
//        }
//
//    }

    fun showToastNetworkError() {
        Toast.makeText(ctx, "Please check the connection", Toast.LENGTH_SHORT).show()
    }

}