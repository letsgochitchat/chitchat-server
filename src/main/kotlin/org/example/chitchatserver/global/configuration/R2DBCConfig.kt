package org.example.chitchatserver.global.configuration

import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions.*
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@EnableR2dbcRepositories
class R2DBCConfig(
    private val r2DBCProperties: R2DBCProperties
) : AbstractR2dbcConfiguration() {

    @Bean
    override fun connectionFactory(): ConnectionFactory {
        val factoryOptions = builder()
            .option(DRIVER, "postgresql")
            .option(HOST, r2DBCProperties.host)
            .option(PORT, r2DBCProperties.port)
            .option(DATABASE, r2DBCProperties.database)
            .option(USER, r2DBCProperties.user)
            .option(PASSWORD, r2DBCProperties.password)
            .option(SSL, false)
            .build()

        return ConnectionFactories.get(factoryOptions)
    }
}

@ConfigurationProperties("spring.r2dbc")
class R2DBCProperties @ConstructorBinding constructor(
    val host: String,
    val port: Int,
    val user: String,
    val password: String,
    val database: String
)