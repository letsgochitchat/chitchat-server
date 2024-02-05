package org.example.chitchatserver.domain.chat.presentation.event

import org.example.chitchatserver.domain.chat.persistence.ChatType
import java.time.LocalDateTime
import java.util.UUID

data class Message(
    val type: ChatType,
    val content: String,
    val sendAt: LocalDateTime = LocalDateTime.now(),
    var roomId: UUID? = null,
    var nickname: String? = null,
    var userId: UUID? = null
) {
    companion object {
        val ERROR = Message(
            type = ChatType.ERROR,
            content = "Invalid Message Format"
        )
    }
}