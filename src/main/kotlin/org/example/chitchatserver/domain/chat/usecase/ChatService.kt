package org.example.chitchatserver.domain.chat.usecase

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.chitchatserver.domain.chat.persistence.ChatEntity
import org.example.chitchatserver.domain.chat.persistence.ChatRepository
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
    private val chatRepository: ChatRepository,
    private val userFacade: UserFacade,
    private val objectMapper: ObjectMapper
) {

    fun execute(session: WebSocketSession): Mono<Void> =
        roomRepository.queryMinConnectionCountRoom()
            .zipWith(userFacade.getCurrentUser())
            .flatMap { (room, currentUser) ->
                val updateRoomOp = roomRepository.save(room.increaseConnectionCount())
                val chatOperations = Mono.zip(
                    receiveMessage(session, room.id, currentUser.id),
                    sendMessage(session, room, currentUser)
                )

                updateRoomOp.then(chatOperations)
                    .publishOn(Schedulers.boundedElastic())
                    .doFinally { roomRepository.save(room.decreaseConnectionCount()).subscribe() }
            }
            .then()

    private fun receiveMessage(session: WebSocketSession, roomId: UUID, currentUserId: UUID): Mono<Void> =
        session.receive()
            .flatMap { it.toMessage(currentUserId) }
            .flatMap { message ->
                val publishOp = redisOperations.convertAndSend(roomId.toString(), message.toJSON())
                val saveOp = chatRepository.save(message.toChatEntity(roomId))
                publishOp.then(saveOp)
            }
            .then()

    private fun sendMessage(session: WebSocketSession, room: RoomEntity, user: UserEntity): Mono<Void> {
        val connectionMessage = Message(
            type = ChatType.CONNECTION,
            content = room.connectionCount.toString(),
            nickname = user.email,
            profileImageUrl = user.profileImageUrl,
            roomId = room.id
        ).toJSON()

        val sendConnectionMessage = session.send(Mono.just(session.textMessage(connectionMessage)))
        val sendChannelMessage = redisOperations.listenToChannel(room.id.toString())
            .map { objectMapper.readValue(it.message as String, Message::class.java) }
            .filter { it.userId != user.id }
            .map { session.textMessage(it.toJSON()) }
            .let { session.send(it) }

        return sendConnectionMessage.thenMany(sendChannelMessage).then()
    }

    fun WebSocketMessage.toMessage(currentUserId: UUID): Mono<Message> =
        Mono.just(objectMapper.readValue(payloadAsText, Message::class.java).apply { this.userId = currentUserId })

    fun Message.toJSON(): String =
        objectMapper.writeValueAsString(this)

    fun Message.toChatEntity(roomId: UUID) =
        ChatEntity(
            userId = this.userId!!,
            roomId = roomId,
            content = this.content,
            nickname = this.nickname,
            profileImageUrl = this.profileImageUrl,
            type = this.type
        )
}