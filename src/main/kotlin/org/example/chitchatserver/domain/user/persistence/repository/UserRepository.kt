package org.example.chitchatserver.domain.user.persistence.repository

import org.example.chitchatserver.domain.user.persistence.UserEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import java.util.UUID

interface UserRepository : ReactiveCrudRepository<UserEntity, UUID> {
    fun findBySub(sub: String): Mono<UserEntity>
}