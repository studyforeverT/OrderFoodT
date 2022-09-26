package com.nvc.orderfood.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nvc.orderfood.utils.Convert.Companion.toMoneyFormat
import java.io.Serializable

@Entity(tableName = "tb_cart")
class Cart(
    @PrimaryKey
    @ColumnInfo(name = "col_id")
    val id : String="",
    @ColumnInfo(name = "col_name")
    val name : String="",
    @ColumnInfo(name = "col_category_id")
    val categoryID : String = "",
    @ColumnInfo(name = "col_description")
    val description : String = "",
    @ColumnInfo(name = "col_image")
    val image : String="",
    @ColumnInfo(name = "col_price")
    val price : Int=0,
    @ColumnInfo(name = "col_quantity")
    var quantity : Int=0,
    @ColumnInfo(name = "col_rate")
    var rating : Float = 0f

) : Serializable{
    fun convertToMoneyFormat() : String{
        return price.toMoneyFormat()
    }
}