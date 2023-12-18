package org.example.chitchatserver.common.exception

import org.example.chitchatserver.common.exception.ResponseStatus.BAD_REQUEST
import org.example.chitchatserver.common.exception.ResponseStatus.CONFLICT
import org.example.chitchatserver.common.exception.ResponseStatus.FORBIDDEN
import org.example.chitchatserver.common.exception.ResponseStatus.NOT_FOUND
import org.example.chitchatserver.common.exception.ResponseStatus.UNAUTHORIZED

class NotFoundException(
    message: String
) : CustomException(NOT_FOUND, message)

class BadRequestException(
    message: String
) : CustomException(BAD_REQUEST, message)

class UnauthorizedException(
    message: String
) : CustomException(UNAUTHORIZED, message)

class ForbiddenException(
    message: String
) : CustomException(FORBIDDEN, message)

class ConflictException(
    message: String
) : CustomException(CONFLICT, message)