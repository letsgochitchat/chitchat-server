package org.example.chitchatserver.thirdparty.webclient.google

import org.example.chitchatserver.common.exception.BadRequestException
import org.example.chitchatserver.common.exception.UnauthorizedException
import org.example.chitchatserver.thirdparty.webclient.google.dto.OauthTokenResponse
import org.example.chitchatserver.thirdparty.webclient.google.dto.UserInfoResponse
import org.example.chitchatserver.thirdparty.webclient.throwError
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class GoogleWebClient(
    private val webClient: WebClient,
    private val googleWebClientProperties: GoogleWebClientProperties,
) {

    companion object {
        private const val CLIENT_ID = "client_id"
        private const val CLIENT_SECRET = "client_secret"
        private const val REDIRECT_URI = "redirect_uri"
        private const val GRANT_TYPE = "grant_type"
        private const val CODE = "code"
        private const val ALT = "alt"
        private const val ACCESS_TOKEN = "access_token"
        private const val TOKEN = "token"
        private const val OAUTH2_BASE_URL = "https://oauth2.googleapis.com"
        private const val GOOGLE_API_BASE_URL = "https://www.googleapis.com"
    }

    fun getGoogleAuthUrl() =
        "https://accounts.google.com/o/oauth2/v2/auth" +
                "?client_id=${googleWebClientProperties.clientId}" +
                "&redirect_uri=${googleWebClientProperties.redirectUri}" +
                "&response_type=code" +
                "&scope=https://www.googleapis.com/auth/userinfo.email+https://www.googleapis.com/auth/userinfo.profile"

    fun getGoogleAccessToken(code: String): Mono<OauthTokenResponse> = webClient
        .mutate()
        .baseUrl(OAUTH2_BASE_URL)
        .build()
        .post()
        .uri {
            it.path("/token")
                .queryParam(CLIENT_ID, googleWebClientProperties.clientId)
                .queryParam(CLIENT_SECRET, googleWebClientProperties.clientSecret)
                .queryParam(REDIRECT_URI, googleWebClientProperties.redirectUri)
                .queryParam(GRANT_TYPE, "authorization_code")
                .queryParam(CODE, code)
                .build()
        }
        .retrieve()
        .throwError(BadRequestException("Code Already Used Or Invalid Code"))
        .bodyToMono(OauthTokenResponse::class.java)

    fun getUserInfo(tokens: OauthTokenResponse): Mono<UserInfoResponse> = webClient
        .mutate()
        .baseUrl(GOOGLE_API_BASE_URL)
        .build()
        .get()
        .uri {
            it.path("/oauth2/v3/userinfo")
                .queryParam(ALT, "json")
                .queryParam(ACCESS_TOKEN, tokens.accessToken)
                .build()
        }
        .retrieve()
        .throwError(UnauthorizedException("Invalid Token"))
        .bodyToMono(UserInfoResponse::class.java)

    fun revokeAccessToken(accessToken: String): Mono<Void> = webClient
        .mutate()
        .baseUrl(OAUTH2_BASE_URL)
        .build()
        .post()
        .uri {
            it.path("/revoke")
                .queryParam(TOKEN, accessToken)
                .build()
        }
        .retrieve()
        .throwError(BadRequestException("Invalid Token"))
        .bodyToMono(Void::class.java)
}
