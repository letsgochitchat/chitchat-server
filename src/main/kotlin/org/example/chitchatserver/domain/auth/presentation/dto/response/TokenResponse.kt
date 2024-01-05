package org.example.chitchatserver.domain.auth.presentation.dto.response

import org.example.chitchatserver.domain.user.persistence.Authority
import java.time.LocalDateTime

data class TokenResponse(
    val accessToken: String,
    val accessExp: LocalDateTime,
    val refreshToken: String,
    val refreshExp: LocalDateTime,
    val authority: Authority,
    val isNew: Boolean,
)
