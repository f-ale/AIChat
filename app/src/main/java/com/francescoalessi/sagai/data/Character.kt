package com.francescoalessi.sagai.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Character(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name:String,
    val attributes:String,
    val isUser:Boolean = false,
)

