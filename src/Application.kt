package com.public.poll

import com.public.poll.module.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.tomcat.*

fun main() {
    val server = embeddedServer(Tomcat, port = 8080) {

        install(DefaultHeaders)
        install(CallLogging)
        install(StatusPages)
        install(ContentNegotiation) {
            json()
        }
        val kodein = kodeinModule()
        databaseModule()
        authModule(kodein)
        maintenanceModule(kodein)
        pollModule(kodein)
    }

    server.start(wait = true)
}