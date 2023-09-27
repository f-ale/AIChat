package com.francescoalessi.sagai.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Conversation(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val characterId:Int = 0,
)