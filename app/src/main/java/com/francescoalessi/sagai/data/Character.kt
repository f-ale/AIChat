package com.francescoalessi.sagai.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.francescoalessi.sagai.data.serialization.UriSerializer
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Character(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name:String,
    val attributes:String,
    val isUser:Boolean = false,
    @Serializable(with = UriSerializer::class) val image: Uri? = null,
)