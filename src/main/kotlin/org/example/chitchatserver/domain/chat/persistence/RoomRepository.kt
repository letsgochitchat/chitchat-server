package org.example.chitchatserver.domain.chat.persistence

import reactor.core.publisher.Mono
import java.util.UUID

interface RoomRepository {
    fun queryById(id: UUID): Mono<RoomEntity>

    fun queryMinConnectionCountRoom(): Mono<RoomEntity>

    fun save(room: RoomEntity): Mono<RoomEntity>
}