package org.example.chitchatserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

internal const val BASE_PACKAGE = "org.example.chitchatserver"

@ConfigurationPropertiesScan
@SpringBootApplication
class ChitchatServerApplication

fun main(args: Array<String>) {
    runApplication<ChitchatServerApplication>(*args)
}
