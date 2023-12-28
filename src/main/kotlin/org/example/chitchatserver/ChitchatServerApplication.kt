package org.example.chitchatserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class ChitchatServerApplication

fun main(args: Array<String>) {
    runApplication<ChitchatServerApplication>(*args)
}
