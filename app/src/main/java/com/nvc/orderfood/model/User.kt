package com.nvc.orderfood.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tb_user")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "col_id")
    var id : String="",
    @ColumnInfo(name = "col_address")
    var addressUser: String? = "",
    @ColumnInfo(name = "col_gender")
    var genderUser: String? = "",
    @ColumnInfo(name = "col_image")
    var imageUser: String? = "",
    @ColumnInfo(name = "col_name")
    var nameUser: String? = "",
    @ColumnInfo(name = "col_phone")
    var phoneUser: String? = "",
    @ColumnInfo(name = "col_timestamp")
    var timestamp: String? = "",
) : Serializable{}