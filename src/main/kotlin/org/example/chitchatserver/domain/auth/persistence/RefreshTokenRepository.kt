package org.example.chitchatserver.domain.auth.persistence

import reactor.core.publisher.Mono

interface RefreshTokenRepository {
    fun save(refreshTokenEntity: RefreshTokenEntity): Mono<Boolean>
}