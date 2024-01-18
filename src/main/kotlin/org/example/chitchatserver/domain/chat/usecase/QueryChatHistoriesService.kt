package org.example.chitchatserver.domain.chat.usecase

import org.example.chitchatserver.common.exception.NotFoundException
import org.example.chitchatserver.domain.chat.persistence.ChatRepository
import org.example.chitchatserver.domain.chat.persistence.RoomRepository
import org.example.chitchatserver.domain.chat.persistence.repository.ChatMongoRepository
import org.example.chitchatserver.domain.chat.persistence.repository.RoomR2DBCRepository
import org.example.chitchatserver.domain.chat.presentation.dto.response.ChatHistoryResponse
import org.example.chitchatserver.domain.chat.presentation.dto.response.QueryChatHistoriesResponse
import org.example.chitchatserver.domain.user.facade.UserFacade
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import java.util.UUID

@Service
class QueryChatHistoriesService(
    private val chatRepository: ChatRepository,
    private val roomRepository: RoomRepository,
    private val userFacade: UserFacade,
) {

    fun execute(roomId: UUID, page: Int): Mono<QueryChatHistoriesResponse> = roomRepository
        .queryById(roomId)
        .switchIfEmpty(Mono.error(NotFoundException("Room Not Found")))
        .zipWith(userFacade.getCurrentUserId())
        .flatMap { (room, currentUserId) ->
            chatRepository.queryByRoomIdOrderBySendAt(room.id, page)
                .map { chat ->
                    ChatHistoryResponse(
                        chat.nickname,
                        chat.profileImageUrl,
                        chat.content,
                        chat.userId == currentUserId,
                        chat.type,
                        chat.sendAt!!,
                    )
                }
                .collectList()
                .flatMap { chats -> Mono.just(QueryChatHistoriesResponse(room.topic, chats)) }
        }
}
