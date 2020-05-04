package com.public.poll

import com.public.poll.mapper.PollMapper
import com.public.poll.mapper.PollMapperImpl
import com.public.poll.mapper.UserMapper
import com.public.poll.mapper.UserMapperImpl
import com.public.poll.module.authModule
import com.public.poll.module.databaseModule
import com.public.poll.module.pollModule
import com.public.poll.repositories.*
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.serialization.DefaultJsonConfiguration
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.tomcat.Tomcat
import kotlinx.serialization.json.Json
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

fun main() {
    val server = embeddedServer(Tomcat, port = 8080) {

        install(DefaultHeaders)
        install(CallLogging)
        install(StatusPages)

        install(ContentNegotiation) {
            json(
                contentType = ContentType.Application.Json,
                json = Json(DefaultJsonConfiguration.copy(prettyPrint = true))
            )
        }

        val kodein = Kodein {
            bind<UserMapper>() with singleton { UserMapperImpl() }
            bind<PollMapper>() with singleton { PollMapperImpl() }

            bind<UserRepository>() with singleton { UserRepositoryImpl(instance()) }
            bind<PollRepository>() with singleton { PollRepositoryImpl(instance()) }
            bind<PollCollectionRepository>() with singleton { PollCollectionRepositoryImpl(instance()) }
        }

        databaseModule()
        authModule(kodein)
        pollModule(kodein)
    }

    server.start(wait = true)
}