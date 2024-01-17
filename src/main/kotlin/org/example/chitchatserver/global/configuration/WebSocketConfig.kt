package org.example.chitchatserver.global.configuration

import org.example.chitchatserver.domain.chat.presentation.ChatHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter

@Configuration
class WebSocketConfig(
    private val chatHandler: ChatHandler
) {
    @Bean
    fun handlerMapping(): HandlerMapping {
        val map = mapOf("/ws/chat" to chatHandler)

        return SimpleUrlHandlerMapping(map, -1)
    }

    @Bean
    fun handlerAdapter() = WebSocketHandlerAdapter()
}