package com.dorox.pinapp.di

import com.dorox.pinapp.data.repository.FireBasePostsRepository
import com.dorox.pinapp.domain.repository.PostsRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindPostsRepository(
        impl: FireBasePostsRepository
    ) : PostsRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirestore() : FirebaseFirestore = FirebaseFirestore.getInstance()

        @Provides
        @Singleton
        fun provideStorage() : FirebaseStorage = FirebaseStorage.getInstance()
    }



}