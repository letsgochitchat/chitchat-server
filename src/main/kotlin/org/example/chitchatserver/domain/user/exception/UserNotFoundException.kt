package org.example.chitchatserver.domain.user.exception

import org.example.chitchatserver.common.exception.CustomException

object UserNotFoundException : CustomException(404, "User Not Found") {
    private fun readResolve(): Any = UserNotFoundException
}