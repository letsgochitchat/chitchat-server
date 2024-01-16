package org.example.chitchatserver.domain.user.facade

import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.UUID

@Component
class UserFacade {

    fun getCurrentUserId(): Mono<UUID> = ReactiveSecurityContextHolder.getContext()
        .flatMap { Mono.just(UUID.fromString(it.authentication.name)) }
}