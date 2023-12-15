package org.example.chitchatserver.common.exception

abstract class CustomException(
    val status: Int,
    override val message: String
): RuntimeException(message)