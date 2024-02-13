package org.example.chitchatserver.domain.chat.persistence

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID

@Document("chat")
class ChatEntity(
    val roomId: UUID,

    val content: String,

    val nickname: String,

    val userId: UUID,

    val type: ChatType,

    @CreatedDate
    val sendAt: LocalDateTime? = null,
)

enum class ChatType {
    MESSAGE, IMAGE, CONNECTION, ERROR, DISCONNECT
}

object ChatFields {
    const val ROOM_ID = "roomId"
    const val CONTENT = "content"
    const val NICKNAME = "nickname"
    const val PROFILE_IMAGE_URL = "profileImageUrl"
    const val USER_ID = "userId"
    const val TYPE = "type"
    const val SEND_AT = "sendAt"
}