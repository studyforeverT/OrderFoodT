package com.nvc.orderfood.di

import com.nvc.orderfood.data.database.cart.CartDao
import com.nvc.orderfood.data.database.category.CategoryDao
import com.nvc.orderfood.data.database.favorite.FavoriteDao
import com.nvc.orderfood.data.database.food.FoodDao
import com.nvc.orderfood.data.database.order.OrderDao
import com.nvc.orderfood.data.database.user.UserDao
import com.nvc.orderfood.data.source.local.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalModule {
    @Provides
    @Singleton
    fun provideFoodLocal(foodDao: FoodDao): FoodLocal {
        return FoodLocal(foodDao)
    }

    @Provides
    @Singleton
    fun provideCartLocal(cartDao: CartDao): CartLocal {
        return CartLocal(cartDao)
    }

    @Provides
    @Singleton
    fun provideCategoryLocal(categoryDao: CategoryDao): CategoryLocal {
        return CategoryLocal(categoryDao)
    }

    @Provides
    @Singleton
    fun provideFavoriteLocal(favoriteDao: FavoriteDao): FavoriteLocal {
        return FavoriteLocal(favoriteDao)
    }

    @Provides
    @Singleton
    fun provideUserLocal(userDao: UserDao): AuthLocal {
        return AuthLocal(userDao)
    }

    @Provides
    @Singleton
    fun provideOrderLocal(orderDao: OrderDao): OrderLocal {
        return OrderLocal(orderDao)
    }

}