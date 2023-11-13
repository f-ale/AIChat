package com.francescoalessi.parla.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.francescoalessi.parla.data.Conversation
import com.francescoalessi.parla.data.Message
import com.francescoalessi.parla.data.Character

data class ConversationWithMessagesAndCharacter (
    @Embedded
    val conversation: Conversation,
    @Relation(
        parentColumn = "id",
        entityColumn = "conversationId"
    )
    val messages: List<Message>,
    @Relation(
        parentColumn = "characterId",
        entityColumn = "id"
    )
    val character: Character
)