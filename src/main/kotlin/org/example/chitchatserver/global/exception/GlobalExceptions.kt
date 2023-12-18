package org.example.chitchatserver.global.exception

import org.example.chitchatserver.common.exception.CustomException
import org.example.chitchatserver.common.exception.ResponseStatus.INTERNAL_SERVER_ERROR


object InternalServerError : CustomException(INTERNAL_SERVER_ERROR, "Internal Server Error") {
    private fun readResolve(): Any = InternalServerError
}