package com.francescoalessi.sagai.data

import android.net.Uri
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.francescoalessi.sagai.data.dao.CharacterDao
import com.francescoalessi.sagai.data.dao.ConversationDao
import com.francescoalessi.sagai.data.dao.MessageDao
import com.francescoalessi.sagai.data.dao.SettingsDao
import java.math.BigDecimal

@Database(entities =
    [
        Character::class,
        Conversation::class,
        Message::class,
        TextGenerationHost::class
    ],
    version = 6)
@TypeConverters(Converters::class)
abstract class SagaiDatabase: RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun conversationDao(): ConversationDao
    abstract fun settingsDao(): SettingsDao
    abstract fun characterDao(): CharacterDao
}
class Converters {
    @TypeConverter
    fun uriFromString(value: String?): Uri? {
        return if (value == null) null else Uri.parse(value)
    }
    @TypeConverter
    fun uriToString(uri: Uri?): String? {
        return uri?.toString()
    }
}