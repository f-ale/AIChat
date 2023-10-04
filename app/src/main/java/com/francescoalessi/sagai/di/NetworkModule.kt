package com.francescoalessi.sagai.di

import com.francescoalessi.sagai.api.TextGenerationService
import com.francescoalessi.sagai.api.interceptors.DynamicUrlInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class TextGenerationApiModule {
    private val baseUrl = "http://10.0.2.2:5000"

    /*
     *   Provides Retrofit Text Generation Service for injection
     */
    @Singleton
    @Provides
    fun provideTextGenerationService(
        okHttpClient: OkHttpClient
    ): TextGenerationService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(Json.asConverterFactory(
                "application/json".toMediaType()
            ))
            .client(okHttpClient)
            .build()
            .create(TextGenerationService::class.java)
    }

    @Provides
    fun provideOkHttpClient(
        dynamicUrlInterceptor: DynamicUrlInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(dynamicUrlInterceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()
    }
}