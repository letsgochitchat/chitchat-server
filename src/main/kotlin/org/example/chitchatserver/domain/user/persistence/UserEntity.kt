package org.example.chitchatserver.domain.user.persistence

import org.example.chitchatserver.common.entity.BaseUUIDEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table(name = "tbl_user")
class UserEntity(
    @get:JvmName("getIdentifier")
    override var id: UUID = UUID(0, 0),

    val email: String,

    val profileImageUrl: String,

    val sub: String,

    val authType: AuthType,

    val authority: Authority,

    @Value("false")
    @field:Transient
    val isNewUser: Boolean = false,
) : BaseUUIDEntity(id) {
}

enum class Authority {
    USER,
    ADMIN
}

enum class AuthType {
    GOOGLE
}