package com.francescoalessi.sagai.di

import com.francescoalessi.sagai.api.TextGenerationService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class TextGenerationModule {
    private val baseUrl = "http://10.0.2.2:5000"

    /*
     *   Provides Retrofit Text Generation Service for injection
     */
    @Singleton
    @Provides
    fun provideTextGenerationService(): TextGenerationService
    {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(Json.asConverterFactory(
                "application/json".toMediaType()
            ))
            .build()
            .create(TextGenerationService::class.java)
    }
}