package com.nvc.orderfood.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabaseInstance(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseStorageInstance(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    @Named("SignUpRef")
    fun provideSignUpRef(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.getReference("Users")
    }

    @Provides
    @Singleton
    @Named("FoodRef")
    fun provideFoodRef(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.getReference("Food")
    }

    @Provides
    @Singleton
    @Named("UsersRef")
    fun provideUserRef(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.getReference("Users")
    }

    @Provides
    @Singleton
    @Named("ProfileRef")
    fun provideProfileRef(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.getReference("Users")
    }

    @Provides
    @Singleton
    @Named("ProfileStorageRef")
    fun provideProfileStorageRef(fireStorageDatabase: FirebaseStorage): StorageReference {
        return fireStorageDatabase.getReference("Users")
    }

    @Provides
    @Singleton
    @Named("OrdersRef")
    fun provideOrdersRef(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.getReference("Orders")
    }

    @Provides
    @Named("CartRef")
    fun provideCartRef(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.getReference("Cart")
    }

    @Provides
    @Singleton
    @Named("FavoriteRef")
    fun provideFavoriteRef(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.getReference("Favorite")

    }

    @Provides
    @Singleton
    @Named("CategoryRef")
    fun provideCategoryRef(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.getReference("Category")
    }

    @Provides
    @Singleton
    @Named("OrderNotificationRef")
    fun provideOOrderNotificationRef(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.getReference("Notification/OrderNotification")
    }

}