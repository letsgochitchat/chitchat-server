package org.example.chitchatserver.domain.auth.persistence.repository

import org.example.chitchatserver.domain.auth.persistence.RefreshTokenEntity
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.Duration

@Repository
class RefreshTokenRepository(
    private val reactiveRedisOperations: ReactiveRedisOperations<String, Any>
) {

    fun save(refreshTokenEntity: RefreshTokenEntity): Mono<Boolean> =
        reactiveRedisOperations.opsForValue().set(
            refreshTokenEntity.token,
            refreshTokenEntity,
            Duration.ofSeconds(refreshTokenEntity.ttl.toLong())
        )
}