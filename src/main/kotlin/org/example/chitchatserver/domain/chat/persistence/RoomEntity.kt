package org.example.chitchatserver.domain.chat.persistence

import org.example.chitchatserver.common.entity.BaseUUIDEntity
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table(name = "tbl_room")
class RoomEntity(
    @get:JvmName("getIdentifier")
    override var id: UUID = UUID(0, 0),

    val topic: String,
) : BaseUUIDEntity(id)