package org.example.chitchatserver.domain.chat.presentation.dto.response

import org.example.chitchatserver.domain.chat.persistence.ChatType
import java.time.LocalDateTime

data class QueryChatHistoriesResponse(
    val name: String,
    val history: List<ChatHistoryResponse>
)

data class ChatHistoryResponse(
    val nickname: String,
    val profileImageUrl: String,
    val content: String,
    val mine: Boolean,
    val type: ChatType,
    val sendAt: LocalDateTime,
)
