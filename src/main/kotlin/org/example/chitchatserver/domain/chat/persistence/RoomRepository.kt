package org.example.chitchatserver.domain.chat.persistence

import reactor.core.publisher.Mono
import java.util.UUID

interface RoomRepository {
    fun queryById(id: UUID): Mono<RoomEntity>
}