package com.francescoalessi.sagai.di

import android.content.Context
import android.net.Uri
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.francescoalessi.sagai.data.Character
import com.francescoalessi.sagai.data.SagaiDatabase
import com.francescoalessi.sagai.data.dao.CharacterDao
import com.francescoalessi.sagai.data.dao.ConversationDao
import com.francescoalessi.sagai.data.dao.MessageDao
import com.francescoalessi.sagai.data.dao.SettingsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.util.Locale
import javax.inject.Provider
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext appContext: Context,
        characterDao: Provider<CharacterDao>
    ): SagaiDatabase {
        val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    // Get the data from the JSON
                    var characterList = readJsonData(appContext)

                    characterList = characterList.map { character ->
                        character.copy(
                            image = Uri.parse(
                                "file:///android_asset/character_images/"
                                        +character.name.removeAllWhitespace().toLowerCase(Locale.ROOT)+".png"
                            )
                        )
                    }
                    // Insert the data into the database
                    characterDao.get().insertAll(characterList)
                }
            }
        }

        return Room.databaseBuilder(
                appContext,
                SagaiDatabase::class.java,
                "SagaiDatabase"
            )
            .addCallback(roomCallback)
            .fallbackToDestructiveMigration() // TODO: Remove and switch to migrations
            .build()
    }
    private suspend fun readJsonData(context: Context): List<Character> {
        val jsonString = withContext(Dispatchers.IO) {
            context.assets.open("characters.json").bufferedReader().use { it.readText() }
        }
        return Json.decodeFromString(jsonString)
    }
    @Provides
    @Singleton
    fun provideMessageDao(sagaiDatabase: SagaiDatabase): MessageDao = sagaiDatabase.messageDao()
    @Provides
    @Singleton
    fun provideConversationDao(sagaiDatabase: SagaiDatabase): ConversationDao = sagaiDatabase.conversationDao()
    @Provides
    @Singleton
    fun provideSettingsDao(sagaiDatabase: SagaiDatabase): SettingsDao = sagaiDatabase.settingsDao()
    @Provides
    @Singleton
    fun provideCharacterDao(sagaiDatabase: SagaiDatabase): CharacterDao = sagaiDatabase.characterDao()
}

fun String.removeAllWhitespace(): String {
    return this.replace("\\s+".toRegex(), "")
}