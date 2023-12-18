package org.example.chitchatserver.domain.user.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table(name = "tbl_user")
class UserEntity(
    @Id
    val id: UUID,

    val accountId: String,

    val email: String,

    val password: String,

    val profileImageUrl: String,

    val authority: Authority
)

enum class Authority {
    USER,
    ADMIN
}