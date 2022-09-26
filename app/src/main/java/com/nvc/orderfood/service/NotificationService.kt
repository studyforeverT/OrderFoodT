package com.nvc.orderfood.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.nvc.orderfood.R
import com.nvc.orderfood.activity.MainActivity
import com.nvc.orderfood.model.Notification
import com.nvc.orderfood.model.Order
import com.nvc.orderfood.model.User
import com.nvc.orderfood.utils.Convert.Companion.toDate
import com.nvc.orderfood.utils.MySharePre
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class NotificationService : Service() {
    val date = Date()

    @Inject
    lateinit var mySharePre: MySharePre

    @Inject
    @Named("OrderNotificationRef")
    lateinit var orderNotificationRef: DatabaseReference
    private lateinit var listenerNotification: ValueEventListener

    @Inject
    @Named("OrdersRef")
    lateinit var orderRef: DatabaseReference
    private var user: User? = null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        sendingNotificationDefault()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sendingNotificationDefault()
        listenerNotification = object : ValueEventListener {
            override fun onDataChange(snapshotNoti: DataSnapshot) {
                try {
                    if (snapshotNoti.hasChildren()) {
                        val notification =
                            snapshotNoti.children.last().getValue(Notification::class.java)
                        val listenerOrder = object : ValueEventListener {
                            override fun onDataChange(snapshotOrder: DataSnapshot) {
                                val order = snapshotOrder.getValue(Order::class.java)
                                if (notification != null && order != null) {
                                    try {
                                        if (notification.timestamp.toDate()!! > date) {
                                            sendingNotificationUser(notification, order)
                                        }
                                    }catch (e : NullPointerException){
                                        e.printStackTrace()
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {}

                        }
                        if (notification != null) {
                            orderRef.child(notification.orderID)
                                .addListenerForSingleValueEvent(listenerOrder)
                        }
                    }
                }catch (e : NullPointerException){
                    e.printStackTrace()
                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        Log.d("TAGGGGG", "onStartCommand: $user")
        user = mySharePre.getUser()
        if (user != null) {
            orderNotificationRef.child(user!!.id).addValueEventListener(listenerNotification)
        }

        return START_STICKY
    }

    private fun sendingNotificationDefault() {
        val remoteViews = RemoteViews(this.packageName, R.layout.custom_notification)
        val notification = NotificationCompat.Builder(
            this, "CHANNEL_NOTIFICATION_ID"
        )
            .setContent(remoteViews)
            .setContentTitle("Order Notification")
            .setContentText("Running...}")
            .build()
        startForeground(1, notification)
    }

    private fun sendingNotificationUser(userNotification: Notification, order: Order) {
        val bundle = Bundle()
        bundle.putSerializable("order", order)
        val pendingIntent = NavDeepLinkBuilder(this)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_main)
            .setDestination(R.id.orderDetailFragment)
            .setArguments(bundle)
            .createPendingIntent()
        val notification = NotificationCompat.Builder(
            this, "CHANNEL_NOTIFICATION_USER_ID"
        )
            .setContentTitle("OrderID: ${order.id}")
            .setContentText("Message: ${userNotification.message}")
            .setSmallIcon(R.drawable.ic_con_notification)
            .setContentIntent(pendingIntent)
            .build()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)
    }


    override fun onDestroy() {
        super.onDestroy()
        if (user != null) {
            orderNotificationRef.child(user!!.id).removeEventListener(listenerNotification)
        }

    }

}