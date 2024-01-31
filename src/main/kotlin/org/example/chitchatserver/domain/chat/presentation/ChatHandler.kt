package org.example.chitchatserver.domain.chat.presentation

import org.example.chitchatserver.domain.chat.usecase.ChatService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Component
class ChatHandler(
    private val chatService: ChatService
) : WebSocketHandler {

    override fun handle(session: WebSocketSession): Mono<Void> =
        chatService.execute(session)
}