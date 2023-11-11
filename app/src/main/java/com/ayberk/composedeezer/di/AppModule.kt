package com.ayberk.composedeezer.di

import com.ayberk.composedeezer.retrofit.RetrofitInstance
import com.ayberk.composedeezer.retrofit.RetrofitRepository
import com.ayberk.composedeezer.util.Constans.BASE_URL
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun firebase_auth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
   fun provideFirebasefirestoreDatabase() =  Firebase.firestore


    @Singleton
    @Provides
    fun provideDeezerRepository(
        api: RetrofitInstance
    ) = RetrofitRepository(api)

    @Singleton
    @Provides
    fun provideDeezerApi(): RetrofitInstance {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(RetrofitInstance::class.java)
    }
}