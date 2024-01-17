package org.example.chitchatserver.domain.chat.usecase

import org.example.chitchatserver.common.exception.NotFoundException
import org.example.chitchatserver.domain.chat.persistence.repository.ChatRepository
import org.example.chitchatserver.domain.chat.persistence.repository.RoomRepository
import org.example.chitchatserver.domain.chat.presentation.dto.response.ChatHistoryResponse
import org.example.chitchatserver.domain.chat.presentation.dto.response.QueryChatHistoriesResponse
import org.example.chitchatserver.domain.user.facade.UserFacade
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class QueryChatHistoriesService(
    private val chatRepository: ChatRepository,
    private val roomRepository: RoomRepository,
    private val userFacade: UserFacade,
) {

    fun execute(roomId: UUID, page: Int): Mono<QueryChatHistoriesResponse> = roomRepository
        .findById(roomId)
        .switchIfEmpty(Mono.error(NotFoundException("Room Not Found")))
        .flatMap { room ->
            val name = room.topic
            userFacade.getCurrentUserId()
                .flatMapMany { currentUserId ->
                    chatRepository.findAllByRoomIdOrderBySendAtDesc(room.id, PageRequest.of(page, 50))
                        .map { chat ->
                            ChatHistoryResponse(
                                chat.nickname,
                                chat.profileImageUrl,
                                chat.content,
                                chat.userId == currentUserId,
                                chat.type,
                                chat.sendAt,
                            )
                        }
                }
                .collectList()
                .flatMap { chats -> Mono.just(QueryChatHistoriesResponse(name, chats)) }
        }
}
