package org.example.chitchatserver.global.security.jwt

import io.jsonwebtoken.Jwts
import org.example.chitchatserver.domain.auth.persistence.RefreshTokenEntity
import org.example.chitchatserver.domain.auth.persistence.RefreshTokenRepository
import org.example.chitchatserver.domain.auth.presentation.dto.response.TokenResponse
import org.example.chitchatserver.domain.user.persistence.Authority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

@Component
class JwtTokenGenerator(
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository,
) {

    fun generateTokens(id: UUID, authority: Authority, isNew: Boolean): Mono<TokenResponse> = Mono.zip(
        generateAccessToken(id, authority),
        generateRefreshToken(id, authority),
    ).map {
        TokenResponse(
            accessToken = it.t1,
            accessExp = LocalDateTime.now().plusSeconds(jwtProperties.accessExp.toLong()),
            refreshToken = it.t2,
            refreshExp = LocalDateTime.now().plusSeconds(jwtProperties.refreshExp.toLong()),
            authority = authority,
            isNew,
        )
    }

    private fun generateRefreshToken(id: UUID, authority: Authority): Mono<String> =
        generateToken(id, authority, TokenType.REFRESH, jwtProperties.refreshExp.toInt())
            .flatMap {
                refreshTokenRepository.save(
                    RefreshTokenEntity(
                        id = id,
                        token = it,
                        authority = authority,
                        jwtProperties.refreshExp.toInt()
                    )
                ).thenReturn(it)
            }

    private fun generateAccessToken(id: UUID, authority: Authority): Mono<String> =
        generateToken(id, authority, TokenType.ACCESS, jwtProperties.accessExp.toInt())

    private fun generateToken(id: UUID, authority: Authority, type: TokenType, exp: Int): Mono<String> = Mono.just(
        Jwts.builder()
            .signWith(jwtProperties.secret)
            .subject(id.toString())
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + (exp * 1000)))
            .claim("type", type.toString())
            .claim("authority", authority)
            .compact()
    )
}