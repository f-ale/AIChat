package com.francescoalessi.parla.di

import com.francescoalessi.parla.api.TextGenerationService
import com.francescoalessi.parla.api.interceptors.DynamicUrlInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


private val json = Json {
    encodeDefaults = true
}

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
            .addConverterFactory(json.asConverterFactory(
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
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(dynamicUrlInterceptor)
            .addInterceptor(logging)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .build()
    }
}