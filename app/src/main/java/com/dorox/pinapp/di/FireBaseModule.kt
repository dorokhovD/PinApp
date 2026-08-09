package com.dorox.pinapp.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface FireBaseModule {

    companion object {
        @Provides
        @Singleton
        fun provideFirestore() : FirebaseFirestore = FirebaseFirestore.getInstance()
    }

}