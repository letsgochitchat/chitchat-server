package org.example.chitchatserver.domain.user.persistence

import org.example.chitchatserver.common.entity.BaseUUIDEntity
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
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

    val authority: Authority
) : BaseUUIDEntity(id)

enum class Authority {
    USER,
    ADMIN
}

enum class AuthType {
    GOOGLE
}