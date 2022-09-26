package com.nvc.orderfood.model

import java.io.Serializable

data class Notification(
    val id : String = "",
    val message : String = "",
    val orderID : String ="",
    val timestamp : String = ""
) : Serializable
