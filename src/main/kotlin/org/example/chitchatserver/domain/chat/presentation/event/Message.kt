package org.example.chitchatserver.domain.chat.presentation.event

import java.time.LocalDateTime

data class Message(
    val type: MessageType,
    val content: String,
    val sendAt: LocalDateTime = LocalDateTime.now(),
    val roomId: String? = null,
    val nickname: String,
    val profileImageUrl: String
)

enum class MessageType {
    MESSAGE, IMAGE, CONNECTION, NEXT, DISCONNECT
}