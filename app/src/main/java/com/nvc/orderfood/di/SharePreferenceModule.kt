package com.nvc.orderfood.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.nvc.orderfood.utils.MySharePre
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SharePreferenceModule {

    @Singleton
    @Provides
    fun provideSharePreference(app: Application) : SharedPreferences {
        return app.applicationContext.getSharedPreferences("My_pre", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideMySharePre(sharePreference: SharedPreferences) : MySharePre {
        return MySharePre(sharePreference)
    }
}