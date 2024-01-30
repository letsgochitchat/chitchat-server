package org.example.chitchatserver.domain.user.facade

import org.example.chitchatserver.domain.user.persistence.UserEntity
import org.example.chitchatserver.global.exception.InvalidTokenException
import org.example.chitchatserver.global.security.jwt.auth.CustomUserDetail
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.UUID

@Component
class UserFacade {

    fun getCurrentUserId(): Mono<UUID> = ReactiveSecurityContextHolder.getContext()
        .flatMap { Mono.just(UUID.fromString(it.authentication.name)) }
        .switchIfEmpty(Mono.error(InvalidTokenException))

    fun getCurrentUser(): Mono<UserEntity> = ReactiveSecurityContextHolder.getContext()
        .map { (it.authentication.principal as CustomUserDetail).user }
        .switchIfEmpty(Mono.error(InvalidTokenException))
}