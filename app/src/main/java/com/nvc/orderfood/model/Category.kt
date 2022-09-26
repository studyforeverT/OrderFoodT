package com.nvc.orderfood.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_category")
data class Category(
    @PrimaryKey
    @ColumnInfo(name = "col_id")
    val id : String="",
    @ColumnInfo(name = "col_name")
    val name : String="",
    @ColumnInfo(name = "col_description")
    val description : String="",
    @ColumnInfo(name = "col_image")
    val image : String="",
)