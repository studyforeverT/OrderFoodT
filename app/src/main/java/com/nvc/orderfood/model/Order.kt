package com.nvc.orderfood.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nvc.orderfood.utils.Convert.Companion.toMoneyFormat
import java.io.Serializable


@Entity(tableName = "tb_order")
data class Order(
    @PrimaryKey
    @ColumnInfo(name = "col_id")
    var id: String = "",
    @ColumnInfo(name = "col_user_id")
    var userID: String = "",
    @ColumnInfo(name = "col_timestamp")
    var timestamp: String = "",
    @ColumnInfo(name = "col_address")
    var address: String = "",
    @ColumnInfo(name = "col_phone")
    var phone: String = "",
    @ColumnInfo(name = "col_receiver_name")
    var receiverName: String = "",
    @ColumnInfo(name = "col_status")
    var status: Int = 2,
    @ColumnInfo(name = "col_total_price")
    var totalPrice: Int = 0
) : Serializable {
    fun convertToMoneyFormat(): String {
        return totalPrice.toMoneyFormat()
    }

    fun convertStatus(): String {
        return when (status) {
            0 -> "Cancel"
            1 -> "Success"
            else -> "Pending"
        }

    }
}