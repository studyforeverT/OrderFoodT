package com.nvc.orderfood.utils

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Constant {
    const val CATEGORY_ALL_ID = "-NBA5PfXQAvnwKmV3BA2"
    const val GO_TO_ORDER_DETAIL = "GO_TO_ORDER_DETAIL"

    val MIGRATION_CART_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE tb_cart ADD COLUMN col_category_id TEXT NOT NULL DEFAULT ''")
            database.execSQL("ALTER TABLE tb_cart ADD COLUMN col_description TEXT NOT NULL DEFAULT ''")
        }
    }


    object OrderStatus {
        const val ORDER_PENDING = 2
        const val ORDER_CANCEL = 0
        const val ORDER_SUCCESS = 1
    }
}