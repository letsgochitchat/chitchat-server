package org.example.chitchatserver.global.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.InvalidClaimException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.example.chitchatserver.global.exception.ExpiredTokenException
import org.example.chitchatserver.global.exception.InternalServerError
import org.example.chitchatserver.global.exception.InvalidTokenException
import org.example.chitchatserver.global.exception.UnexpectedTokenException
import org.example.chitchatserver.global.security.jwt.auth.CustomUserDetailService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JwtParser(
    private val userDetailService: CustomUserDetailService,
    private val jwtProperties: JwtProperties
) {
    fun getAuthentication(token: String): Mono<Authentication> {
        val claims = getClaims(token)

        if (claims.header.type != TokenConstraints.ACCESS) {
            throw InvalidTokenException
        }

        return getUserDetail(claims.payload)
            .map {
                UsernamePasswordAuthenticationToken(it, "", it.authorities)
            }
    }

    private fun getClaims(token: String): Jws<Claims> =
        try {
            Jwts.parser()
                .verifyWith(jwtProperties.secret)
                .build()
                .parseSignedClaims(token)
        } catch (e: Exception) {
            when (e) {
                is InvalidClaimException -> throw InvalidTokenException
                is ExpiredJwtException -> throw ExpiredTokenException
                is JwtException -> throw UnexpectedTokenException
                else -> throw InternalServerError
            }
        }

    private fun getUserDetail(claim: Claims): Mono<UserDetails> =
        userDetailService.findByUsername(claim.subject)
}