package org.example.chitchatserver.global.security.jwt

import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import javax.crypto.SecretKey

@ConfigurationProperties("jwt")
class JwtProperties @ConstructorBinding constructor(
    val header: String,
    val prefix: String,
    val accessExp: Int,
    val refreshExp: Int,
    secret: String
) {
    val secret: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray())
}