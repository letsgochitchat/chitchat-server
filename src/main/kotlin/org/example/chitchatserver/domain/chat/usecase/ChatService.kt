package org.example.chitchatserver.domain.chat.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.chitchatserver.domain.chat.persistence.ChatType
import org.example.chitchatserver.domain.chat.persistence.RoomEntity
import org.example.chitchatserver.domain.chat.persistence.RoomRepository
import org.example.chitchatserver.domain.chat.presentation.event.Message
import org.example.chitchatserver.domain.user.facade.UserFacade
import org.example.chitchatserver.domain.user.persistence.UserEntity
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Service
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import java.util.UUID

@Service
class ChatService(
    private val roomRepository: RoomRepository,
    private val redisOperations: ReactiveRedisOperations<String, Any>,
    private val userFacade: UserFacade,
    private val objectMapper: ObjectMapper
) {

    fun execute(session: WebSocketSession, nickname: String): Mono<Void> =
        roomRepository.queryMinConnectionCountRoom()
            .zipWith(userFacade.getCurrentUser())
            .flatMap { (room, currentUser) ->
                val updateRoomOp = roomRepository.save(room.increaseConnectionCount())
                val chatOperations = Mono.zip(
                    receiveMessage(session, room.id, currentUser.id, nickname),
                    sendMessage(session, room, currentUser)
                ).publishOn(Schedulers.boundedElastic())
                    .doFinally { roomRepository.save(room.decreaseConnectionCount()).subscribe() }

                updateRoomOp.then(chatOperations)
            }
            .then()

    private fun receiveMessage(
        session: WebSocketSession,
        roomId: UUID,
        currentUserId: UUID,
        nickname: String
    ): Mono<Void> =
        session.receive()
            .flatMap { it.toMessage(currentUserId, roomId, nickname) }
            .defaultIfEmpty(Message.ERROR)
            .filter { it.type != ChatType.ERROR }
            .flatMap { redisOperations.convertAndSend(roomId.toString(), it.toJSON()) }
            .then()

    private fun sendMessage(session: WebSocketSession, room: RoomEntity, user: UserEntity): Mono<Void> {
        val connectionMessage = Message.createConnectionMessage(
            connectionCount = room.connectionCount,
            roomId = room.id
        )

        val sendConnectionMessage = session.send(connectionMessage)
        val sendChannelMessage = redisOperations.listenToChannel(room.id.toString())
            .map { objectMapper.readValue(it.message as String, Message::class.java) }
            .filter { it.userId != user.id }
            .flatMap { session.send(it) }

        return sendConnectionMessage.thenMany(sendChannelMessage).then()
    }

    fun WebSocketMessage.toMessage(currentUserId: UUID, roomId: UUID, nickname: String): Mono<Message> =
        try {
            Mono.just(
                objectMapper.readValue(payloadAsText, Message::class.java)
                    .apply {
                        this.userId = currentUserId
                        this.roomId = roomId
                        this.nickname = nickname
                    }
            )
        } catch (e: Exception) {
            Mono.empty()
        }

    fun WebSocketSession.send(message: Message) =
        send(Mono.just(textMessage(objectMapper.writeValueAsString(message))))

    fun Message.toJSON(): String =
        objectMapper.writeValueAsString(this)
}