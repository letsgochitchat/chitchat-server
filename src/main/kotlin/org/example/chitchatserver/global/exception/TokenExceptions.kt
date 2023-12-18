package org.example.chitchatserver.global.exception

import org.example.chitchatserver.common.exception.CustomException
import org.example.chitchatserver.common.exception.ResponseStatus.UNAUTHORIZED

object InvalidTokenException : CustomException(UNAUTHORIZED, "Invalid Token") {
    private fun readResolve(): Any = InvalidTokenException
}

object ExpiredTokenException : CustomException(UNAUTHORIZED, "Expired Token") {
    private fun readResolve(): Any = ExpiredTokenException
}

object UnexpectedTokenException : CustomException(UNAUTHORIZED, "Unexpected Token") {
    private fun readResolve(): Any = UnexpectedTokenException
}