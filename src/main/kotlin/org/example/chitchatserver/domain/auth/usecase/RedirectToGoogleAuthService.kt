package org.example.chitchatserver.domain.auth.usecase

import org.example.chitchatserver.thirdparty.webclient.google.GoogleWebClient
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.net.URI

@Service
class RedirectToGoogleAuthService(
    private val googleWebClient: GoogleWebClient,
) {

    fun execute(): ResponseEntity<Void> {
        val headers = HttpHeaders()
        headers.location = URI.create(googleWebClient.getGoogleAuthUrl())
        return ResponseEntity(headers, HttpStatus.MOVED_PERMANENTLY)
    }
}