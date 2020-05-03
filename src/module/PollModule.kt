package com.public.poll.module

import com.public.poll.dto.CreatedPollDto
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing

fun Application.pollModule() {
    routing {
        route("/api") {

            authenticate {
                route("/poll") {

                    get("/feed") {
                        call.respond(com.public.poll.handler.poll.list.PollFeedHandler().handle())
                    }

                    get("/my") {
                        call.respond(
                            com.public.poll.handler.poll.list.PollsMyListHandler().handle(
                                user = call.getUser()
                            )
                        )
                    }

                    get("/history") {
                        call.respond(
                            com.public.poll.handler.poll.list.PollHistoryHandler().handle(
                                user = call.getUser()
                            )
                        )
                    }

                    post("/create") {
                        val createdPollDto = call.receive<CreatedPollDto>()
                        call.respond(
                            com.public.poll.handler.poll.crud.PollCreateHandler().handle(
                                user = call.getUser(),
                                pollDto = createdPollDto
                            )
                        )
                    }

                    get("/get/{pollId}") {
                        com.public.poll.handler.poll.crud.PollGetHandler()
                            .handle(pollId = call.getPollId())?.let { pollDto ->
                                call.respond(pollDto)
                            } ?: run {
                            call.respond(io.ktor.http.HttpStatusCode.BadRequest)
                        }
                    }

                    post("/edit/{pollId}") {
                        val pollDto = call.receive<CreatedPollDto>()
                        val handled = com.public.poll.handler.poll.crud.PollEditHandler().handle(
                            pollId = call.getPollId(),
                            pollDto = pollDto
                        )
                        if (handled) {
                            call.respond(io.ktor.http.HttpStatusCode.Accepted)
                        } else {
                            call.respond(io.ktor.http.HttpStatusCode.BadRequest)
                        }
                    }

                    post("/engage/{pollId}") {
                        val handled = com.public.poll.handler.poll.action.PollEngageHandler().handle(
                            user = call.getUser(), pollId = call.getPollId()
                        )
                        if (handled) {
                            call.respond(io.ktor.http.HttpStatusCode.Created)
                        } else {
                            call.respond(io.ktor.http.HttpStatusCode.BadRequest)
                        }
                    }

                    post("/vote/{pollId}") {
                        val handled = com.public.poll.handler.poll.action.PollVoteHandler().handle(
                            user = call.getUser(), pollId = call.getPollId()
                        )
                        if (handled) {
                            call.respond(io.ktor.http.HttpStatusCode.Created)
                        } else {
                            call.respond(io.ktor.http.HttpStatusCode.BadRequest)
                        }
                    }

                    post("/report/{pollId}") {
                        val handled = com.public.poll.handler.poll.action.PollReportHandler().handle(
                            user = call.getUser(), pollId = call.getPollId()
                        )
                        if (handled) {
                            call.respond(io.ktor.http.HttpStatusCode.Created)
                        } else {
                            call.respond(io.ktor.http.HttpStatusCode.BadRequest)
                        }
                    }

                    post("/like/{pollId}/add") {
                        val handled = com.public.poll.handler.poll.action.like.PollAddLikeHandler().handle(
                            user = call.getUser(), pollId = call.getPollId()
                        )
                        if (handled) {
                            call.respond(io.ktor.http.HttpStatusCode.Created)
                        } else {
                            call.respond(io.ktor.http.HttpStatusCode.BadRequest)
                        }
                    }

                    post("/like/{pollId}/remove") {
                        com.public.poll.handler.poll.action.like.PollRemoveLikeHandler().handle(
                            user = call.getUser(), pollId = call.getPollId()
                        )
                        call.respond(io.ktor.http.HttpStatusCode.OK)
                    }

                    post("/dislike/{pollId}/add") {
                        val handled = com.public.poll.handler.poll.action.dislike.PollAddDislikeHandler().handle(
                            user = call.getUser(), pollId = call.getPollId()
                        )
                        if (handled) {
                            call.respond(io.ktor.http.HttpStatusCode.Created)
                        } else {
                            call.respond(io.ktor.http.HttpStatusCode.BadRequest)
                        }
                    }

                    post("/dislike/{pollId}/remove") {
                        com.public.poll.handler.poll.action.dislike.PollRemoveDislikeHandler().handle(
                            user = call.getUser(), pollId = call.getPollId()
                        )
                        call.respond(io.ktor.http.HttpStatusCode.OK)
                    }
                }
            }
        }
    }
}