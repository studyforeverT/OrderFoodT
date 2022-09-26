package com.nvc.orderfood.di

import android.app.Application
import androidx.room.Room
import com.nvc.orderfood.data.database.cart.CartDao
import com.nvc.orderfood.data.database.cart.CartDatabase
import com.nvc.orderfood.data.database.category.CategoryDao
import com.nvc.orderfood.data.database.category.CategoryDatabase
import com.nvc.orderfood.data.database.favorite.FavoriteDao
import com.nvc.orderfood.data.database.favorite.FavoriteDatabase
import com.nvc.orderfood.data.database.food.FoodDao
import com.nvc.orderfood.data.database.food.FoodDatabase
import com.nvc.orderfood.data.database.order.OrderDao
import com.nvc.orderfood.data.database.order.OrderDatabase
import com.nvc.orderfood.utils.Constant
import com.nvc.orderfood.data.database.user.UserDao
import com.nvc.orderfood.data.database.user.UserDatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideFoodDatabase(app: Application): FoodDatabase {
        return Room.databaseBuilder(
            app.applicationContext, FoodDatabase::class.java, "food.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFoodDao(db: FoodDatabase): FoodDao {
        return db.foodDao()
    }

    @Provides
    @Singleton
    fun provideCartDatabase(app: Application): CartDatabase {
        return Room.databaseBuilder(
            app.applicationContext, CartDatabase::class.java, "cart.db"
        ).addMigrations(Constant.MIGRATION_CART_1_2).build()
    }

    @Provides
    @Singleton
    fun provideCartDao(db: CartDatabase): CartDao {
        return db.cartDao()
    }

    @Provides
    @Singleton
    fun provideCategoryDatabase(app: Application): CategoryDatabase {
        return Room.databaseBuilder(
            app.applicationContext, CategoryDatabase::class.java, "category.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(db: CategoryDatabase): CategoryDao {
        return db.categoryDao()
    }


    @Provides
    @Singleton
    fun provideFavoriteDatabase(app: Application): FavoriteDatabase {
        return Room.databaseBuilder(
            app.applicationContext, FavoriteDatabase::class.java, "favorite.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(db: FavoriteDatabase): FavoriteDao {
        return db.favoriteDao()
    }

    @Provides
    @Singleton
    fun provideUserDatabase(app: Application): UserDatabase {
        return Room.databaseBuilder(
            app.applicationContext, UserDatabase::class.java, "user.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(db: UserDatabase): UserDao {
        return db.userDao()
    }

    @Provides
    @Singleton
    fun provideOrderDatabase(app: Application): OrderDatabase {
        return Room.databaseBuilder(
            app.applicationContext, OrderDatabase::class.java, "order.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideOrderDao(db: OrderDatabase): OrderDao {
        return db.orderDao()
    }
//    @Provides
//    @Singleton
//    fun provideOrderItemDatabase(app: Application): OrderItemDatabase {
//        return Room.databaseBuilder(
//            app.applicationContext, OrderItemDatabase::class.java, "order_item.db"
//        ).build()
//    }
//    @Provides
//    @Singleton
//    fun provideOrderItemDao(db : OrderItemDatabase) : OrderItemDao{
//        return db.orderItemDao()
//    }
}