package com.francescoalessi.sagai.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Character(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name:String,
    val attributes:String,
    val isUser:Boolean = false,
    val image: Uri? = null,
)

