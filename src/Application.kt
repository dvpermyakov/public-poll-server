package com.public.poll

import com.public.poll.dto.CreatedPollDto
import com.public.poll.poll.CreatePollHandler
import com.public.poll.poll.GetPollHandler
import com.public.poll.poll.PollFeedHandler
import com.public.poll.table.PollTable
import com.public.poll.table.UserTable
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.serialization.DefaultJsonConfiguration
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.tomcat.Tomcat
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    val server = embeddedServer(Tomcat, port = 8080) {
        install(ContentNegotiation) {
            json(
                contentType = ContentType.Application.Json,
                json = Json(
                    DefaultJsonConfiguration.copy(
                        prettyPrint = true
                    )
                )
            )
        }
        connectDB()
        createTables()

        routing {
            route("/api/poll") {

                get("/feed") {
                    val handler = PollFeedHandler()
                    call.respond(handler.handle())
                }

                get("/get/{pollId}") {
                    val handler = GetPollHandler()
                    val pollId = requireNotNull(call.parameters["pollId"])
                    call.respond(handler.handle(pollId))
                }

                post("/create") {
                    val createdPollDto = call.receive<CreatedPollDto>()
                    val handler = CreatePollHandler()
                    call.respond(handler.handle(createdPollDto))
                }
            }
        }
    }

    server.start(wait = true)
}

private fun connectDB() {
    Database.connect(
        url = "jdbc:h2:~/test",
        driver = "org.h2.Driver",
        user = "root",
        password = ""
    )
}

private fun createTables() {
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(UserTable)
        SchemaUtils.create(PollTable)
    }
}
