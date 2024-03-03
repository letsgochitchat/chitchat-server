package org.example.chitchatserver.domain.chat.facade

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.chitchatserver.domain.chat.persistence.ChatType
import org.example.chitchatserver.domain.chat.presentation.event.Message
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Component
import java.util.Timer
import java.util.TimerTask
import java.util.UUID

@Component
class ChatTimer(
    private val redisOperations: ReactiveRedisOperations<String, Any>,
    private val objectMapper: ObjectMapper
) {
    private val timers: HashMap<UUID, TimerTask> = HashMap()
    private val timer = Timer(true)

    fun setTimer(roomId: UUID, period: Long) {
        if (timers.containsKey(roomId)) {
            val task = timers[roomId]!!
            task.cancel()
        }
        timers[roomId] = object : TimerTask() {
            override fun run() {
                val timeoutMessage = Message(
                    type = ChatType.TIMEOUT,
                    content = "Timeout",
                    roomId = roomId
                )

                redisOperations.convertAndSend(roomId.toString(), objectMapper.writeValueAsString(timeoutMessage)).subscribe()
                timers.remove(roomId)
            }
        }
        timer.schedule(timers[roomId], period * 1000)
    }
}