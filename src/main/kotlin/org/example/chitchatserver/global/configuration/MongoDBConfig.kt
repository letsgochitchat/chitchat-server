package org.example.chitchatserver.global.configuration

import com.mongodb.reactivestreams.client.MongoClient
import org.example.chitchatserver.BASE_PACKAGE
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@Configuration
@EnableReactiveMongoAuditing
@EnableReactiveMongoRepositories(basePackages = [BASE_PACKAGE], reactiveMongoTemplateRef = "customMongoTemplate")
class MongoDBConfig(
    @Value("\${spring.data.mongodb.database}")
    private val database: String,
    private val mongoClient: MongoClient
) {

    @Bean
    fun customMongoTemplate(): ReactiveMongoTemplate =
         ReactiveMongoTemplate(mongoClient, database)
}