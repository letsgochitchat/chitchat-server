package org.example.chitchatserver.global.exception

data class ErrorResponse(
    val status: Int,
    val message: String,
)

data class BindErrorResponse(
    val status: Int,
    val fields: Map<String, String?>
)