package org.example.chitchatserver.domain.chat.persistence

import reactor.core.publisher.Flux
import java.util.UUID

interface ChatRepository {
    fun queryByRoomIdOrderBySendAt(roomId: UUID, page: Int, limit: Int = 50): Flux<ChatEntity>
}