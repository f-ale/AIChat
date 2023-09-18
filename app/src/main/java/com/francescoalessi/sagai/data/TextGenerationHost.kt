package com.francescoalessi.sagai.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TextGenerationHost (
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val ip4address:String, // TODO: Make into InetAddress. Support ipv6.
    val ip4port:String
)