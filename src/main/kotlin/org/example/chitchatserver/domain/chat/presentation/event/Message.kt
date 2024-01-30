package org.example.chitchatserver.domain.chat.presentation.event

import org.example.chitchatserver.domain.chat.persistence.ChatType
import java.time.LocalDateTime
import java.util.UUID

data class Message(
    val type: ChatType,
    val content: String,
    val sendAt: LocalDateTime = LocalDateTime.now(),
    val roomId: UUID? = null,
    val nickname: String,
    val profileImageUrl: String,
    var userId: UUID? = null
)