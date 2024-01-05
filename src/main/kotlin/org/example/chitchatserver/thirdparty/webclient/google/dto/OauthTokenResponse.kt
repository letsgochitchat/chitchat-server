package org.example.chitchatserver.thirdparty.webclient.google.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OauthTokenResponse(
    val accessToken: String,
    val idToken: String,
)