package org.example.chitchatserver.global.security.jwt

import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class JwtFilter(
    private val jwtParser: JwtParser,
    private val jwtProperties: JwtProperties
) : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val token = extractTokenFromHeader(exchange.request)
        token?.let {
            return jwtParser.getAuthentication(token).flatMap {
                chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(it))
            }
        }

        return chain.filter(exchange)
    }

    private fun extractTokenFromHeader(request: ServerHttpRequest): String? =
        request.headers.getFirst(jwtProperties.header)?.also {
            if (it.startsWith(jwtProperties.prefix)) {
                return it.substring(jwtProperties.prefix.length)
            }
        }
}