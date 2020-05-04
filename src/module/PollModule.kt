package com.public.poll.module

import com.public.poll.handler.poll.action.PollEngageHandler
import com.public.poll.handler.poll.action.PollReportHandler
import com.public.poll.handler.poll.action.PollVoteHandler
import com.public.poll.handler.poll.crud.PollCreateHandler
import com.public.poll.handler.poll.crud.PollEditHandler
import com.public.poll.handler.poll.crud.PollGetHandler
import com.public.poll.handler.poll.list.PollFeedHandler
import com.public.poll.handler.poll.list.PollHistoryHandler
import com.public.poll.handler.poll.list.PollsMyListHandler
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

            authenticate {
                route("/poll") {

                    get("/feed") {
                        val handler by kodein.instance<PollFeedHandler>()
                        call.commonRespond(handler.handle())
                    }

                    get("/my") {
                        val handler by kodein.instance<PollsMyListHandler>()
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

                    get("/get/{pollId}") {
                        val handler by kodein.instance<PollGetHandler>()
                        call.commonRespond(
                            handler.handle(pollId = call.getPollId())
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
                        call.commonRespond(
                            PollEngageHandler().handle(
                                user = call.getUser(),
                                pollId = call.getPollId()
                            )
                        )
                    }

                    post("/vote/{pollId}/{answerId}") {
                        call.commonRespond(
                            PollVoteHandler().handle(
                                user = call.getUser(),
                                pollId = call.getPollId(),
                                answerId = call.getAnswerId()
                            )
                        )
                    }

                    post("/report/{pollId}") {
                        call.commonRespond(
                            PollReportHandler().handle(
                                user = call.getUser(),
                                pollId = call.getPollId()
                            )
                        )
                    }

                }
            }
        }
    }
}