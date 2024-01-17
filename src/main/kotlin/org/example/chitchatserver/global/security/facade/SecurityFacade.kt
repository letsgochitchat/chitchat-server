package org.example.chitchatserver.global.security.facade

import org.example.chitchatserver.global.security.jwt.auth.CustomUserDetail
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SecurityFacade {

    fun getCurrentUser() =
        ReactiveSecurityContextHolder.getContext()
            .map {
                (it.authentication.principal as CustomUserDetail).user
            }
}