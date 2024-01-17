package org.example.chitchatserver.domain.chat.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.example.chitchatserver.domain.chat.persistence.repository.RoomRepository
import org.example.chitchatserver.domain.chat.presentation.event.Message
import org.example.chitchatserver.domain.chat.presentation.event.MessageType
import org.example.chitchatserver.global.security.facade.SecurityFacade
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Service
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Service
class ChatService(
    private val roomRepository: RoomRepository,
    private val redisOperations: ReactiveRedisOperations<String, String>,
    private val securityFacade: SecurityFacade,
    private val objectMapper: ObjectMapper
) {

    fun execute(session: WebSocketSession): Mono<Void> =
        mono {
            val room = roomRepository.findFirstByOrderByConnectionCountDesc().awaitSingle()
            val roomId = room.id.toString()
            val user = securityFacade.getCurrentUser().awaitSingle()

            Mono.zip(
                session.receive()
                    .map { it.toMessage() }
                    .flatMap {
                        redisOperations.convertAndSend(roomId, it.toJSON())
                    }.then(),
                session.send(
                    Mono.just(
                        session.textMessage(
                            Message(
                                type = MessageType.CONNECTION,
                                content = roomId,
                                nickname = user.email,
                                profileImageUrl = user.profileImageUrl,
                                roomId = roomId
                            ).toJSON()
                        )
                    )
                ).thenMany(
                    session.send(
                        redisOperations.listenToChannel(roomId)
                            .map { session.textMessage(it.message) }
                    )
                ).then()
            ).then().awaitSingle()
        }

    fun WebSocketMessage.toMessage(): Message =
        objectMapper.readValue(payloadAsText, Message::class.java)

    fun Message.toJSON(): String =
        objectMapper.writeValueAsString(this)
}