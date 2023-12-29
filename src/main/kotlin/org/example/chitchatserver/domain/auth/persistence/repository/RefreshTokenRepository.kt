package org.example.chitchatserver.domain.auth.persistence.repository

import org.example.chitchatserver.domain.auth.persistence.RefreshTokenEntity
import reactor.core.publisher.Mono

interface RefreshTokenRepository {
    fun save(refreshTokenEntity: RefreshTokenEntity): Mono<Boolean>
}