package org.example.chitchatserver.domain.user.persistence.repository

import org.example.chitchatserver.domain.user.persistence.UserEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import java.util.*

interface UserRepository : ReactiveCrudRepository<UserEntity, UUID> {
}