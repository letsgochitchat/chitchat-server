package org.example.chitchatserver.domain.chat.persistence

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID

@Document("chat")
class ChatEntity(

    @Id
    val id: UUID,

    val roomId: UUID,

    val content: String,

    val nickname: String,

    val profileImageUrl: String,

    val userId: UUID,

    val type: ChatType,

    @CreatedDate
    val sendAt: LocalDateTime,
)

enum class ChatType {
    MESSAGE, IMAGE, CONNECTION
}