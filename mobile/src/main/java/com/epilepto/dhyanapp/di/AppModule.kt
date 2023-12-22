package com.epilepto.dhyanapp.di

import android.content.Context
import com.epilepto.dhyanapp.data.remote.FirebaseMessagingResponse
import com.epilepto.dhyanapp.data.repository.FirebaseRepository
import com.epilepto.dhyanapp.data.repository.FirebaseSessionRepository
import com.epilepto.dhyanapp.utils.Constants
import com.epilepto.dhyanapp.utils.DataStoreUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStoreUtils(
        @ApplicationContext context: Context
    ) = DataStoreUtils(context)

//    @Provides
//    @Singleton
//    fun provideBluetoothController(
//        @ApplicationContext context: Context
//    ) : BluetoothController = BluetoothControllerImpl(context)

    @Provides
    @Singleton
    fun provideFCMService(): FirebaseMessagingResponse {
       return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideFirebaseUserRepository(
        fcmService: FirebaseMessagingResponse
    ) = FirebaseRepository(fcmService)

    @Provides
    @Singleton
    fun provideFirebaseSessionRepository(): FirebaseSessionRepository = FirebaseSessionRepository()
}