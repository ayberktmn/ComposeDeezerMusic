package com.ayberk.composedeezer.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun firebase_auth() = FirebaseAuth.getInstance()

    //@Provides
   // @Singleton
  // fun provideFirebasefirestoreDatabase() =  Firebase.firestore
}