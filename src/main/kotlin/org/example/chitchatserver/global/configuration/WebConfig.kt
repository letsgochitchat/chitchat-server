package org.example.chitchatserver.global.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class WebConfig : WebFluxConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedHeaders("GET", "POST", "PUT", "PATCH", "DELETE", "HEAD", "OPTIONS")
            .allowCredentials(true)
            .maxAge(3600)
    }
}