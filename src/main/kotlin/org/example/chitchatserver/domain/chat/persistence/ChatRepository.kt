package org.example.chitchatserver.domain.chat.persistence

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

interface ChatRepository {
    fun queryByRoomIdOrderBySendAt(roomId: UUID, page: Int, limit: Int = 50): Flux<ChatEntity>

    fun save(chat: ChatEntity): Mono<ChatEntity>
}