package com.nvc.orderfood.utils

import android.annotation.SuppressLint
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class Convert {

    companion object {
        @SuppressLint("SimpleDateFormat")
        private val formatter = SimpleDateFormat("HH:mm:ss dd/MM/yyyy")

        fun Int.toMoneyFormat(): String {
            val m: Double = this.toDouble()
            val formatter = DecimalFormat("###,###,###")
            return formatter.format(m)
        }

        fun toTimeStampString(): String {
            return try {
                formatter.format(
                    Timestamp(System.currentTimeMillis())
                ).toString()
            } catch (e: Exception) {
                ""
            }
        }

        fun String.toDate(): Date? {
            return try {
                formatter.parse(this)
            } catch (e: Exception) {
                null
            }
        }
    }
}