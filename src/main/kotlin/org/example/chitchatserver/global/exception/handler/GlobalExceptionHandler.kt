package org.example.chitchatserver.global.exception.handler

import org.example.chitchatserver.common.exception.BadRequestException
import org.example.chitchatserver.common.exception.CustomException
import org.example.chitchatserver.global.exception.BindErrorResponse
import org.example.chitchatserver.global.exception.ErrorResponse
import org.example.chitchatserver.global.exception.InternalServerError
import org.example.chitchatserver.common.exception.ResponseStatus.BAD_REQUEST
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.reactive.function.server.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebInputException
import reactor.core.publisher.Mono

@Component
@Order(-2)
class GlobalExceptionHandler(
    errorAttributes: ErrorAttributes,
    webProperties: WebProperties,
    applicationContext: ApplicationContext,
    serverCodecConfigurer: ServerCodecConfigurer
) : AbstractErrorWebExceptionHandler(
    errorAttributes,
    webProperties.resources,
    applicationContext
) {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    init {
        super.setMessageReaders(serverCodecConfigurer.readers)
        super.setMessageWriters(serverCodecConfigurer.writers)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes?): RouterFunction<ServerResponse> =
        RouterFunctions.route(RequestPredicates.all(), this::handleError)

    private fun handleError(request: ServerRequest): Mono<ServerResponse> =
        when (val exception = getError(request)) {
            is CustomException -> exception.toErrorResponse()
            is WebExchangeBindException -> exception.toFieldErrorResponse()
            is ServerWebInputException -> BadRequestException(exception.message).toErrorResponse()
            is ResponseStatusException -> BadRequestException("Handler Not Found: ${request.method()} ${request.path()}").toErrorResponse()
            else -> {
                logger.error(exception.message, exception)
                InternalServerError.toErrorResponse()
            }
        }

}

fun WebExchangeBindException.toFieldErrorResponse(): Mono<ServerResponse> {
    val fieldErrors = HashMap<String, String?>()

    this.fieldErrors.forEach {
        fieldErrors[it.field] = it.defaultMessage
    }

    return ServerResponse
        .status(BAD_REQUEST)
        .bodyValue(
            BindErrorResponse(
                status = BAD_REQUEST,
                fields = fieldErrors
            )
        )
}

fun CustomException.toErrorResponse() = ServerResponse
    .status(this.status)
    .bodyValue(
        ErrorResponse(
            status = this.status,
            message = this.message
        )
    )