package com.francescoalessi.sagai.data

import android.net.Uri
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.math.BigDecimal

@Database(entities = [Character::class, Conversation::class, Message::class, TextGenerationHost::class], version = 3)
@TypeConverters(Converters::class)
abstract class SagaiDatabase: RoomDatabase() {
    abstract fun messageDao(): MessageDao
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