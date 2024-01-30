package org.example.chitchatserver.domain.chat.persistence

import org.example.chitchatserver.common.entity.BaseUUIDEntity
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table(name = "tbl_room")
class RoomEntity(
    @get:JvmName("getIdentifier")
    override var id: UUID = UUID(0, 0),

    val topic: String,

    var connectionCount: Int
) : BaseUUIDEntity(id) {

    fun increaseConnectionCount(): RoomEntity {
        this.connectionCount += 1
        return this
    }

    fun decreaseConnectionCount(): RoomEntity {
        this.connectionCount -= 1
        return this
    }
}