package org.example.chitchatserver.domain.chat.persistence.repository

import org.example.chitchatserver.domain.chat.persistence.RoomEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import java.util.UUID

interface RoomRepository : ReactiveCrudRepository<RoomEntity, UUID> {
}