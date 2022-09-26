package com.nvc.orderfood.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.nvc.orderfood.data.source.firebase.*
import com.google.firebase.storage.StorageReference
import com.nvc.orderfood.utils.MySharePre
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RemoteModule {

    @Provides
    @Singleton
    fun provideAuthRemote(
        @Named("UsersRef")
        authRef: DatabaseReference,
        firebaseAuth: FirebaseAuth
    ): AuthRemote {
        return AuthRemote(authRef, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideSignUpRemote(
        @Named("SignUpRef")
        signUpRef: DatabaseReference,
        firebaseAuth: FirebaseAuth
    )
            : SignUpRemote {
        return SignUpRemote(signUpRef, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideFoodRemote(
        @Named("FoodRef")
        foodRef: DatabaseReference
    )
            : FoodRemote {
        return FoodRemote(foodRef)
    }

    @Provides
    @Singleton
    fun provideCartRemote(
        @Named("CartRef")
        cartRef: DatabaseReference,
        @Named("FoodRef")
        foodRef: DatabaseReference,
        mySharePre: MySharePre
    )
            : CartRemote {
        return CartRemote(cartRef, foodRef, mySharePre)
    }

    @Provides
    @Singleton
    fun provideFavoriteRemote(
        @Named("FavoriteRef")
        favoriteRef: DatabaseReference,
        @Named("FoodRef")
        foodRef: DatabaseReference,
        mySharePre: MySharePre
    )
            : FavoriteRemote {
        return FavoriteRemote(favoriteRef, foodRef, mySharePre)
    }


    @Provides
    @Singleton
    fun provideProfileRemote(
        @Named("ProfileRef")
        profileRef: DatabaseReference,
        @Named("ProfileStorageRef")
        profileStorageRef: StorageReference,
        auth: FirebaseAuth
    )
            : ProfileRemote {
        return ProfileRemote(profileRef, profileStorageRef, auth)
    }


    @Provides
    @Singleton
    fun provideCategoryRemote(
        @Named("CategoryRef")
        categoryRef: DatabaseReference
    )
            : CategoryRemote {
        return CategoryRemote(categoryRef)
    }

    @Provides
    @Singleton
    fun providePaymentRemote(
        @Named("OrdersRef")
        ordersRef: DatabaseReference,
        @Named("CartRef")
        cartRef: DatabaseReference,
        @Named("FoodRef")
        foodRef: DatabaseReference,
        mySharePre: MySharePre
    )
            : OrderRemote {
        return OrderRemote(ordersRef, cartRef, foodRef, mySharePre)
    }

    @Provides
    @Singleton
    fun provideOrderRemote(
        @Named("OrdersRef")
        orderRef: DatabaseReference,
        auth: FirebaseAuth
    )
            : OrderHistoryRemote {
        return OrderHistoryRemote(orderRef, auth)
    }

    @Provides
    @Singleton
    fun provideNotificationRemote(
        @Named("OrderNotificationRef")
        orderNotificationRef: DatabaseReference,
        @Named("OrdersRef")
        orderRef: DatabaseReference,
        mySharePre: MySharePre
    )
            : NotificationRemote {
        return NotificationRemote(orderNotificationRef, orderRef, mySharePre)
    }
}