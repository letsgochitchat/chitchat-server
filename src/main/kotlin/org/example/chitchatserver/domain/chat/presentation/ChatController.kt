package org.example.chitchatserver.domain.chat.presentation

import org.example.chitchatserver.domain.chat.presentation.dto.response.QueryChatHistoriesResponse
import org.example.chitchatserver.domain.chat.usecase.QueryChatHistoriesService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID

@RequestMapping("/chat")
@RestController
class ChatController(
    private val queryChatHistoriesService: QueryChatHistoriesService,
) {

    @GetMapping("/history/{room-id}")
    fun queryChatHistory(
        @PathVariable("room-id") roomId: UUID,
        @RequestParam("page", defaultValue = "1") page: Int
    ): Mono<QueryChatHistoriesResponse> =
        queryChatHistoriesService.execute(roomId, page - 1)
}