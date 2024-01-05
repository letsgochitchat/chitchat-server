package org.example.chitchatserver.domain.auth.presentation

import org.example.chitchatserver.domain.auth.presentation.dto.response.TokenResponse
import org.example.chitchatserver.domain.auth.usecase.GoogleLoginService
import org.example.chitchatserver.domain.auth.usecase.RedirectToGoogleAuthService
import org.jetbrains.annotations.NotNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RequestMapping("/auth")
@RestController
class AuthController(
    private val redirectToGoogleAuthService: RedirectToGoogleAuthService,
    private val googleLoginService: GoogleLoginService
) {

    @GetMapping("/oauth/google")
    fun redirectToGoogleAuth(): ResponseEntity<Void> =
        redirectToGoogleAuthService.execute()

    @PostMapping("/google")
    fun login(
        @RequestParam("code") @NotNull code: String,
    ): Mono<TokenResponse> = googleLoginService.execute(code)
}