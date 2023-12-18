package org.example.chitchatserver.global.security.jwt.auth

import org.example.chitchatserver.domain.user.persistence.repository.UserRepository
import org.example.chitchatserver.global.exception.InvalidTokenException
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.UUID

@Component
class CustomUserDetailService(
    private val userRepository: UserRepository
) : ReactiveUserDetailsService {

    override fun findByUsername(username: String?): Mono<UserDetails> =
        userRepository.findById(UUID.fromString(username))
            .switchIfEmpty(Mono.error(InvalidTokenException))
            .map {
                CustomUserDetail(
                    userId = it.id,
                    authority = it.authority
                )
            }
}