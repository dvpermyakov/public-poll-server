package com.public.poll.module

import com.public.poll.handler.poll.action.PollEngageHandler
import com.public.poll.handler.poll.action.PollReportHandler
import com.public.poll.handler.poll.action.PollVoteHandler
import com.public.poll.handler.poll.crud.PollCreateHandler
import com.public.poll.handler.poll.crud.PollEditHandler
import com.public.poll.handler.poll.crud.PollGetHandler
import com.public.poll.handler.poll.list.PollFeedHandler
import com.public.poll.handler.poll.list.PollHistoryHandler
import com.public.poll.handler.poll.list.PollMyListHandler
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receive
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

fun Application.pollModule(kodein: Kodein) {
    routing {
        route("/api") {
            route("/poll") {

                get("/feed") {
                    val handler by kodein.instance<PollFeedHandler>()
                    call.commonRespond(handler.handle())
                }

                get("/get/{pollId}") {
                    val handler by kodein.instance<PollGetHandler>()
                    call.commonRespond(
                        handler.handle(pollId = call.getPollId())
                    )
                }

                authenticate {

                    get("/my") {
                        val handler by kodein.instance<PollMyListHandler>()
                        call.commonRespond(
                            handler.handle(userDto = call.getUser())
                        )
                    }

                    get("/history") {
                        val handler by kodein.instance<PollHistoryHandler>()
                        call.commonRespond(
                            handler.handle(userDto = call.getUser())
                        )
                    }

                    post("/create") {
                        val handler by kodein.instance<PollCreateHandler>()
                        call.commonRespond(
                            handler.handle(
                                userDto = call.getUser(),
                                createdPollDto = call.receive()
                            )
                        )
                    }

                    post("/edit/{pollId}") {
                        val handler by kodein.instance<PollEditHandler>()
                        call.commonRespond(
                            handler.handle(
                                userDto = call.getUser(),
                                pollId = call.getPollId(),
                                createdPollDto = call.receive()
                            )
                        )
                    }

                    post("/engage/{pollId}") {
                        val handler by kodein.instance<PollEngageHandler>()
                        call.commonRespond(
                            handler.handle(
                                userDto = call.getUser(),
                                pollId = call.getPollId()
                            )
                        )
                    }

                    post("/vote/{pollId}/{answerId}") {
                        val handler by kodein.instance<PollVoteHandler>()
                        call.commonRespond(
                            handler.handle(
                                userDto = call.getUser(),
                                pollId = call.getPollId(),
                                answerId = call.getAnswerId()
                            )
                        )
                    }

                    post("/report/{pollId}") {
                        val handler by kodein.instance<PollReportHandler>()
                        call.commonRespond(
                            handler.handle(
                                userDto = call.getUser(),
                                pollId = call.getPollId()
                            )
                        )
                    }

                }
            }
        }
    }
}