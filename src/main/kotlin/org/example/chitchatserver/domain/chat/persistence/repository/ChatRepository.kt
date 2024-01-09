package org.example.chitchatserver.domain.chat.persistence.repository

import org.example.chitchatserver.domain.chat.persistence.ChatEntity
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import java.util.UUID

interface ChatRepository : ReactiveMongoRepository<ChatEntity, UUID> {
}