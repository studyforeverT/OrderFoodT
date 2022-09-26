package com.nvc.orderfood.di

import com.google.firebase.database.DatabaseReference
import com.nvc.orderfood.data.source.firebase.*
import com.nvc.orderfood.data.source.local.*
import com.nvc.orderfood.repository.*
import com.nvc.orderfood.utils.CheckNetwork
import com.nvc.orderfood.utils.MySharePre
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        authRemote: AuthRemote,
        checkNetwork: CheckNetwork,
        mySharePre: MySharePre
    ): AuthRepository {
        return AuthRepository(authRemote, checkNetwork, mySharePre)
    }

    @Provides
    @Singleton
    fun provideSignUpRepository(
        signupRemote: SignUpRemote,
        checkNetwork: CheckNetwork,
        authLocal: AuthLocal
    ): SignUpRepository {
        return SignUpRepository(signupRemote, checkNetwork, authLocal)
    }

    @Provides
    @Singleton
    fun provideFoodRepository(foodRemote: FoodRemote, foodLocal: FoodLocal): FoodRepository {
        return FoodRepository(foodRemote, foodLocal)
    }


    @Provides
    @Singleton
    fun provideCartRepository(
        checkNetwork: CheckNetwork,
        cartRemote: CartRemote,
        cartLocal: CartLocal
    ): CartRepository {
        return CartRepository(checkNetwork, cartRemote, cartLocal)
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(
        checkNetwork: CheckNetwork,
        favoriteRemote: FavoriteRemote,
        favoriteLocal: FavoriteLocal
    ): FavoriteRepository {
        return FavoriteRepository(checkNetwork, favoriteRemote, favoriteLocal)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(profileRemote: ProfileRemote): ProfileRepository {
        return ProfileRepository(profileRemote)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(
        categoryRemote: CategoryRemote,
        categoryLocal: CategoryLocal
    ): CategoryRepository {
        return CategoryRepository(categoryRemote, categoryLocal)
    }

    @Provides
    @Singleton
    fun providePaymentRepository(
        orderRemote: OrderRemote,
    ): OrderRepository {
        return OrderRepository(orderRemote)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(
        orderLocal: OrderLocal,
        orderHistoryRemote: OrderHistoryRemote
    ): OrderHistoryRepository {
        return OrderHistoryRepository(orderHistoryRemote, orderLocal)
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(
        notificationRemote: NotificationRemote,
        checkNetwork: CheckNetwork
    ): NotificationRepository {
        return NotificationRepository(notificationRemote, checkNetwork)
    }


    @Provides
    @Singleton
    fun provideSearchRepository(
        @Named("FoodRef")
        searchRef: DatabaseReference,
    ): SearchRepository {
        return SearchRepository(searchRef)
    }
}