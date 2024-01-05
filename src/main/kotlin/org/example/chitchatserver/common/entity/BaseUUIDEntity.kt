package org.example.chitchatserver.common.entity

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import java.util.UUID

abstract class BaseUUIDEntity(
    @Id
    @get:JvmName("getIdentifier")
    open var id: UUID = UUID(0, 0)
) : Persistable<UUID> {
    override fun getId(): UUID = id

    override fun isNew(): Boolean = (id == UUID(0, 0)).also {
        if (it) id = UUID.randomUUID()
    }
}