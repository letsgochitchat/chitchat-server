package org.example.chitchatserver.domain.chat.persistence.impl

import org.example.chitchatserver.domain.chat.persistence.RoomEntity
import org.example.chitchatserver.domain.chat.persistence.RoomRepository
import org.example.chitchatserver.domain.chat.persistence.repository.RoomR2DBCRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
class RoomRepositoryImpl(
    private val roomR2DBCRepository: RoomR2DBCRepository
) : RoomRepository {

    override fun queryById(id: UUID): Mono<RoomEntity> =
        roomR2DBCRepository.findById(id)

    override fun queryMinConnectionCountRoom(): Mono<RoomEntity> =
        roomR2DBCRepository.findFirstByOrderByConnectionCountAsc()

    override fun save(room: RoomEntity): Mono<RoomEntity> =
        roomR2DBCRepository.save(room)
}