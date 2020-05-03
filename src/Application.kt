package com.public.poll

import com.public.poll.dao.UserDao
import com.public.poll.dto.CreatedPollDto
import com.public.poll.poll.action.PollEngageHandler
import com.public.poll.poll.action.PollReportHandler
import com.public.poll.poll.action.PollVoteHandler
import com.public.poll.poll.action.dislike.PollAddDislikeHandler
import com.public.poll.poll.action.dislike.PollRemoveDislikeHandler
import com.public.poll.poll.action.like.PollAddLikeHandler
import com.public.poll.poll.action.like.PollRemoveLikeHandler
import com.public.poll.poll.crud.PollCreateHandler
import com.public.poll.poll.crud.PollEditHandler
import com.public.poll.poll.crud.PollGetHandler
import com.public.poll.poll.list.PollFeedHandler
import com.public.poll.poll.list.PollHistoryHandler
import com.public.poll.poll.list.PollsMyListHandler
import com.public.poll.table.PollTable
import com.public.poll.table.UserTable
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
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
import org.joda.time.DateTime
import java.util.*

data class UserPrincipal(val user: UserDao) : Principal

fun main() {
    val server = embeddedServer(Tomcat, port = 8080) {

        install(DefaultHeaders)
        install(CallLogging)
        install(StatusPages)

        install(Authentication) {
            basic {
                realm = "own realm for basic"
                validate { credentials ->
                    transaction {
                        UserDao.find { UserTable.name eq credentials.name }.firstOrNull()
                    }?.let { user ->
                        if (user.password.contentEquals(credentials.password)) {
                            UserPrincipal(user)
                        } else null
                    }
                }
            }
        }

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
            route("/api") {

                get("/auth/signin") {
                    transaction {
                        UserDao.new {
                            created = DateTime.now()
                            name = "Dmitrii"
                            password = Base64.getEncoder().encodeToString("password".toByteArray())
                            email = "dv@gmail.com"
                        }
                    }
                }

                get("/auth/signup") {

                }

                authenticate {
                    route("/poll") {

                        get("/feed") {
                            call.respond(PollFeedHandler().handle())
                        }

                        get("/my") {
                            call.respond(
                                PollsMyListHandler().handle(
                                    user = call.getUser()
                                )
                            )
                        }

                        get("/history") {
                            call.respond(
                                PollHistoryHandler().handle(
                                    user = call.getUser()
                                )
                            )
                        }

                        post("/create") {
                            val createdPollDto = call.receive<CreatedPollDto>()
                            call.respond(
                                PollCreateHandler().handle(
                                    user = call.getUser(),
                                    pollDto = createdPollDto
                                )
                            )
                        }

                        get("/get/{pollId}") {
                            PollGetHandler().handle(pollId = call.getPollId())?.let { pollDto ->
                                call.respond(pollDto)
                            } ?: run {
                                call.respond(HttpStatusCode.NotFound)
                            }
                        }

                        post("/edit/{pollId}") {
                            val pollDto = call.receive<CreatedPollDto>()
                            call.respond(
                                PollEditHandler().handle(
                                    pollId = call.getPollId(),
                                    pollDto = pollDto
                                )
                            )
                        }

                        post("/engage/{pollId}") {
                            val handled = PollEngageHandler().handle(
                                user = call.getUser(), pollId = call.getPollId()
                            )
                            if (handled) {
                                call.respond(HttpStatusCode.Created)
                            } else {
                                call.respond(HttpStatusCode.NotFound)
                            }
                        }

                        post("/vote/{pollId}") {
                            val handled = PollVoteHandler().handle(
                                user = call.getUser(), pollId = call.getPollId()
                            )
                            if (handled) {
                                call.respond(HttpStatusCode.Created)
                            } else {
                                call.respond(HttpStatusCode.NotFound)
                            }
                        }

                        post("/report/{pollId}") {
                            val handled = PollReportHandler().handle(
                                user = call.getUser(), pollId = call.getPollId()
                            )
                            if (handled) {
                                call.respond(HttpStatusCode.Created)
                            } else {
                                call.respond(HttpStatusCode.NotFound)
                            }
                        }

                        post("/like/{pollId}/add") {
                            val handled = PollAddLikeHandler().handle(
                                user = call.getUser(), pollId = call.getPollId()
                            )
                            if (handled) {
                                call.respond(HttpStatusCode.Created)
                            } else {
                                call.respond(HttpStatusCode.NotFound)
                            }
                        }

                        post("/like/{pollId}/remove") {
                            PollRemoveLikeHandler().handle(
                                user = call.getUser(), pollId = call.getPollId()
                            )
                            call.respond(HttpStatusCode.OK)
                        }

                        post("/dislike/{pollId}/add") {
                            val handled = PollAddDislikeHandler().handle(
                                user = call.getUser(), pollId = call.getPollId()
                            )
                            if (handled) {
                                call.respond(HttpStatusCode.Created)
                            } else {
                                call.respond(HttpStatusCode.NotFound)
                            }
                        }

                        post("/dislike/{pollId}/remove") {
                            PollRemoveDislikeHandler().handle(
                                user = call.getUser(), pollId = call.getPollId()
                            )
                            call.respond(HttpStatusCode.OK)
                        }
                    }
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

private fun ApplicationCall.getUser(): UserDao {
    return requireNotNull(authentication.principal<UserPrincipal>()).user
}

private fun ApplicationCall.getPollId(): UUID {
    return UUID.fromString(requireNotNull(parameters["pollId"]))
}