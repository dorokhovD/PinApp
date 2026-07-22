package com.dorox.courseapp.di

import com.dorox.courseapp.data.CoursesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object  CoursesApiService {

    @Provides
    @Singleton
    fun apiService(): CoursesApi {
        val url = "https://drive.usercontent.google.com/"
        return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CoursesApi::class.java)
    }
}
