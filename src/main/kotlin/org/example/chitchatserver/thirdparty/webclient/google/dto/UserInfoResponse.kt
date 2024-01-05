package org.example.chitchatserver.thirdparty.webclient.google.dto

data class UserInfoResponse(
    val sub: String,
    val name: String,
    val picture: String,
    val email: String,
)