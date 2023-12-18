package org.example.chitchatserver.global.exception

import org.example.chitchatserver.common.exception.CustomException

object InvalidTokenException : CustomException(401, "Invalid Token") {
    private fun readResolve(): Any = InvalidTokenException
}

object ExpiredTokenException : CustomException(401, "Expired Token") {
    private fun readResolve(): Any = ExpiredTokenException
}

object UnexpectedTokenException : CustomException(401, "Unexpected Token") {
    private fun readResolve(): Any = UnexpectedTokenException
}