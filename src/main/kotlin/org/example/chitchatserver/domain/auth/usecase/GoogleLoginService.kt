package org.example.chitchatserver.domain.auth.usecase

import org.example.chitchatserver.domain.auth.presentation.dto.response.TokenResponse
import org.example.chitchatserver.domain.user.persistence.AuthType
import org.example.chitchatserver.domain.user.persistence.Authority
import org.example.chitchatserver.domain.user.persistence.UserEntity
import org.example.chitchatserver.domain.user.persistence.repository.UserRepository
import org.example.chitchatserver.global.security.jwt.JwtTokenGenerator
import org.example.chitchatserver.thirdparty.webclient.google.GoogleWebClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class GoogleLoginService(
    private val userRepository: UserRepository,
    private val googleWebClient: GoogleWebClient,
    private val jwtTokenGenerator: JwtTokenGenerator,
) {

    fun execute(code: String): Mono<TokenResponse> = googleWebClient
        .getGoogleAccessToken(code)
        .flatMap { tokens ->
            googleWebClient.getUserInfo(tokens)
                .doFinally { googleWebClient.revokeAccessToken(tokens.accessToken) }
        }
        .flatMap {
            userRepository.findBySub(it.sub)
                .switchIfEmpty(
                    userRepository.save(
                        UserEntity(
                            email = it.email,
                            profileImageUrl = it.picture,
                            authType = AuthType.GOOGLE,
                            sub = it.sub,
                            authority = Authority.USER,
                            isNewUser = true
                        )
                    )
                )
        }
        .flatMap {
            jwtTokenGenerator.generateTokens(it.id, it.authority, it.isNewUser)
        }
}
