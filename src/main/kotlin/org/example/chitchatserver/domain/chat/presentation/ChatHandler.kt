package org.example.chitchatserver.domain.chat.presentation

import org.example.chitchatserver.domain.chat.usecase.ChatService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.CloseStatus
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono

@Component
class ChatHandler(
    private val chatService: ChatService
) : WebSocketHandler {

    override fun handle(session: WebSocketSession): Mono<Void> {
        val nickname = session.parseNickname()
            ?: return session.close(CloseStatus.create(1007, "Nickname must be Provided"))

        return chatService.execute(session, nickname)
    }

    fun WebSocketSession.parseNickname(): String? {
        val urlComponent = UriComponentsBuilder.fromUri(this.handshakeInfo.uri).build()
        val params = urlComponent.queryParams

        return params.getFirst("nickname")
    }
}