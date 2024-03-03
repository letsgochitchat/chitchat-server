package org.example.chitchatserver.global.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.example.chitchatserver.domain.chat.facade.ChatTimer
import org.example.chitchatserver.domain.chat.persistence.ChatEntity
import org.example.chitchatserver.domain.chat.persistence.ChatRepository
import org.example.chitchatserver.domain.chat.persistence.ChatType
import org.example.chitchatserver.domain.chat.presentation.event.Message
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GlobalChatListener(
    private val redisOperations: ReactiveRedisOperations<String, Any>,
    private val chatTimer: ChatTimer,
    private val chatRepository: ChatRepository,
    private val objectMapper: ObjectMapper
) {

    @PostConstruct
    fun listen(): Mono<Void> {
        redisOperations.listenToPattern("*")
            .map { objectMapper.readValue(it.message as String, Message::class.java) }
            .filter { it.type != ChatType.TIMEOUT }
            .flatMap {
                chatTimer.setTimer(it.roomId!!, 30)
                chatRepository.save(it.toChatEntity())
            }.subscribe()

        return Mono.empty()
    }

    fun Message.toChatEntity() =
        ChatEntity(
            userId = this.userId!!,
            roomId = this.roomId!!,
            content = this.content,
            nickname = this.nickname!!,
            type = this.type
        )


}