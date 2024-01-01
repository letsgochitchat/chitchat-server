package org.example.chitchatserver.thirdparty.webclient.google

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties("google")
class GoogleWebClientProperties @ConstructorBinding constructor(
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String,
)