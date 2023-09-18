package com.francescoalessi.sagai.di

import android.content.Context
import androidx.room.Room
import com.francescoalessi.sagai.data.MessageDao
import com.francescoalessi.sagai.data.SagaiDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): SagaiDatabase {
        return Room.databaseBuilder(
            appContext,
            SagaiDatabase::class.java,
            "SagaiDatabase"
        ).fallbackToDestructiveMigration() // TODO: Remove and switch to migrations
            .build()
    }
    @Provides
    @Singleton
    fun provideRecipeDao(sagaiDatabase: SagaiDatabase): MessageDao = sagaiDatabase.messageDao()
}