package org.example.chitchatserver.domain.chat.persistence.repository

import org.example.chitchatserver.domain.chat.persistence.RoomEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import java.util.UUID

interface RoomR2DBCRepository : ReactiveCrudRepository<RoomEntity, UUID> {
    fun findFirstByOrderByConnectionCountAsc(): Mono<RoomEntity>
}