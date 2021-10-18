package com.public.poll.module

import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class Connection(val session: DefaultWebSocketSession) {
    companion object {
        var lastId = AtomicInteger(0)
    }
    val name = "user${lastId.getAndIncrement()}"
}

fun Application.roomModule() {
    install(WebSockets)
    val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
    routing {
        webSocket("/room") {
            val thisConnection = Connection(this)
            connections += thisConnection
            try {
                send("You are connected!")
                for (frame in incoming) {
                    log.info("type = ${frame.frameType}")
                    when (frame) {
                        is Frame.Text -> {
                            val text = "${thisConnection.name}=${frame.readText()}"
                            connections.forEach {
                                it.session.send(text)
                            }
                        }
                        else -> Unit
                    }
                }
            } finally {
                connections -= thisConnection
            }
        }
    }
}