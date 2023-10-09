package com.francescoalessi.sagai.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.francescoalessi.sagai.data.Conversation
import com.francescoalessi.sagai.data.Message
import com.francescoalessi.sagai.data.Character

data class ConversationWithCharacter (
    @Embedded
    val conversation: Conversation,
    @Relation(
        parentColumn = "characterId",
        entityColumn = "id"
    )
    val character: Character
)