package com.nvc.orderfood.di

import android.app.Application
import com.nvc.orderfood.utils.CheckNetwork
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideCheckNetwork(app: Application): CheckNetwork {
        return CheckNetwork(app.applicationContext)
    }
}