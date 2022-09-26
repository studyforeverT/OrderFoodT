package com.nvc.orderfood.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nvc.orderfood.utils.Convert.Companion.toMoneyFormat
import java.io.Serializable

@Entity(tableName = "tb_order_item"
)
data class OrderItem(
    @PrimaryKey
    @ColumnInfo(name = "col_id")
    val id : String="",
    @ColumnInfo(name = "col_order_id")
    val orderID : String="",
    @ColumnInfo(name = "col_food_id")
    val foodID : String="",
    @ColumnInfo(name = "col_name")
    val name : String="",
    @ColumnInfo(name = "col_image")
    val image : String="",
    @ColumnInfo(name = "col_price")
    val price : Int=0,
    @ColumnInfo(name = "col_quantity")
    val quantity : Int=0,
    @ColumnInfo(name = "col_rate")
    var rate : Int = 0

):Serializable{
    fun convertToMoneyFormat() : String{
        return price.toMoneyFormat()
    }
}
