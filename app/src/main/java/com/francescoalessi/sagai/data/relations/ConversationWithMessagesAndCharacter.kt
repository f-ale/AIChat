package com.francescoalessi.sagai.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.francescoalessi.sagai.data.Conversation
import com.francescoalessi.sagai.data.Message
import com.francescoalessi.sagai.data.Character

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