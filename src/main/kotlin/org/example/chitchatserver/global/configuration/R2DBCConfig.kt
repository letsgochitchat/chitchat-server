package org.example.chitchatserver.global.configuration

import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions.DATABASE
import io.r2dbc.spi.ConnectionFactoryOptions.DRIVER
import io.r2dbc.spi.ConnectionFactoryOptions.HOST
import io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD
import io.r2dbc.spi.ConnectionFactoryOptions.PORT
import io.r2dbc.spi.ConnectionFactoryOptions.SSL
import io.r2dbc.spi.ConnectionFactoryOptions.USER
import io.r2dbc.spi.ConnectionFactoryOptions.builder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import java.nio.ByteBuffer
import java.util.UUID


@Configuration
@EnableR2dbcRepositories
class R2DBCConfig(
    private val r2DBCProperties: R2DBCProperties
) : AbstractR2dbcConfiguration() {

    @Bean
    override fun connectionFactory(): ConnectionFactory {
        val factoryOptions = builder()
            .option(DRIVER, "mysql")
            .option(HOST, r2DBCProperties.host)
            .option(PORT, r2DBCProperties.port)
            .option(DATABASE, r2DBCProperties.database)
            .option(USER, r2DBCProperties.user)
            .option(PASSWORD, r2DBCProperties.password)
            .option(SSL, false)
            .build()

        return ConnectionFactories.get(factoryOptions)
    }

    @Bean
    override fun getCustomConverters(): MutableList<Any> =
        mutableListOf(UUIDToByteArrayConverter(), ByteArrayToUUIDConverter())

    @WritingConverter
    class UUIDToByteArrayConverter : Converter<UUID?, ByteArray?> {
        override fun convert(source: UUID): ByteArray {
            val bb: ByteBuffer = ByteBuffer.wrap(ByteArray(16))
            bb.putLong(source.mostSignificantBits)
            bb.putLong(source.leastSignificantBits)
            return bb.array()
        }
    }

    @ReadingConverter
    class ByteArrayToUUIDConverter : Converter<ByteArray?, UUID?> {
        override fun convert(source: ByteArray): UUID =
            UUID.nameUUIDFromBytes(source)
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