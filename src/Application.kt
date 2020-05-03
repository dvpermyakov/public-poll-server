package com.public.poll

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
    }

    server.start(wait = true)
}