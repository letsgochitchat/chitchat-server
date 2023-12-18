package org.example.chitchatserver.domain.user.exception

import org.example.chitchatserver.common.exception.CustomException
import org.example.chitchatserver.common.exception.ResponseStatus.NOT_FOUND

object UserNotFoundException : CustomException(NOT_FOUND, "User Not Found") {
    private fun readResolve(): Any = UserNotFoundException
}