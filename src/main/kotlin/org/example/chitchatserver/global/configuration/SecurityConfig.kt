package org.example.chitchatserver.global.configuration

import org.example.chitchatserver.global.security.jwt.JwtFilter
import org.example.chitchatserver.global.security.jwt.JwtParser
import org.example.chitchatserver.global.security.jwt.JwtProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    private val jwtParser: JwtParser,
    private val jwtProperties: JwtProperties
) {

    @Bean
    protected fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http {
            authorizeExchange {
                authorize(anyExchange, denyAll)
            }
            formLogin { disable() }
            csrf { disable() }
            httpBasic { disable() }
            addFilterAt(JwtFilter(jwtParser, jwtProperties), SecurityWebFiltersOrder.HTTP_BASIC)
        }

    @Bean
    protected fun passwordEncoder() = BCryptPasswordEncoder()
}