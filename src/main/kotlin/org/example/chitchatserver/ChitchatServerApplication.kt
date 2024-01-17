package org.example.chitchatserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing

@EnableReactiveMongoAuditing
@ConfigurationPropertiesScan
@SpringBootApplication
class ChitchatServerApplication

fun main(args: Array<String>) {
    runApplication<ChitchatServerApplication>(*args)
}
