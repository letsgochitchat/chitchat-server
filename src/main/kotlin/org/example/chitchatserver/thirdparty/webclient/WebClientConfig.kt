package org.example.chitchatserver.thirdparty.webclient

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.example.chitchatserver.common.exception.CustomException
import org.example.chitchatserver.global.exception.InternalServerError
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatusCode
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import java.time.Duration
import java.util.concurrent.TimeUnit

@Configuration
class WebClientConfig {

    @Bean
    fun webClient(): WebClient {
        val httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .responseTimeout(Duration.ofMillis(5000))
            .doOnConnected {
                it.addHandlerLast(ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                    .addHandlerLast(WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))
            }

        val webClient = WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .defaultHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
            .build()

        return webClient
    }
}

fun WebClient.ResponseSpec.throwError(vararg exception: CustomException) =
    this.onStatus(HttpStatusCode::is4xxClientError) { response ->
        exception.forEach {
            if (response.statusCode().value() == it.status) {
                return@onStatus Mono.error<CustomException>(it)
            }
        }
        return@onStatus Mono.error(InternalServerError)
    }
