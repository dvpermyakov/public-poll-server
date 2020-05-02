package com.public.poll

import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.tomcat.Tomcat

fun main(args: Array<String>) {
    val server = embeddedServer(Tomcat, port = 8080) {
        routing {
            get("/api/poll/feed") {
                call.respondText("HELLO WORLD!\n")
            }
        }
    }
    server.start(wait = true)
}
