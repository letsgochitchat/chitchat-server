package org.example.chitchatserver.domain.chat.persistence.impl

import org.example.chitchatserver.domain.chat.persistence.ChatEntity
import org.example.chitchatserver.domain.chat.persistence.ChatFields
import org.example.chitchatserver.domain.chat.persistence.ChatRepository
import org.example.chitchatserver.domain.chat.persistence.repository.ChatMongoRepository
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.UUID

@Repository
class ChatRepositoryImpl(
    private val chatMongoRepository: ChatMongoRepository,
    private val mongoTemplate: ReactiveMongoTemplate
) : ChatRepository {

    override fun queryByRoomIdOrderBySendAt(roomId: UUID, page: Int, limit: Int): Flux<ChatEntity> {
        val criteria = Criteria.where(ChatFields.ROOM_ID).`is`(roomId)
        val sort = Sort.by(Sort.Direction.DESC, ChatFields.SEND_AT)
        val query = Query(criteria)
            .skip((page * limit).toLong())
            .limit(limit)
            .with(sort)

        return mongoTemplate.find(query)
    }
}