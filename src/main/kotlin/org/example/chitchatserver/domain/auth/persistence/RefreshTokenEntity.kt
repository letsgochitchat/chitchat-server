package org.example.chitchatserver.domain.auth.persistence

import org.example.chitchatserver.domain.user.persistence.Authority
import java.util.UUID

class RefreshTokenEntity(

    val id: UUID,

    val token: String,

    val authority: Authority,

    val ttl: Int,
)