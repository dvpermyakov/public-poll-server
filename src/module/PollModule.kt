package com.public.poll.module

import com.public.poll.dto.CreatedPollDto
import com.public.poll.handler.poll.action.PollEngageHandler
import com.public.poll.handler.poll.action.PollReportHandler
import com.public.poll.handler.poll.action.PollVoteHandler
import com.public.poll.handler.poll.action.dislike.PollAddDislikeHandler
import com.public.poll.handler.poll.action.dislike.PollRemoveDislikeHandler
import com.public.poll.handler.poll.action.like.PollAddLikeHandler
import com.public.poll.handler.poll.action.like.PollRemoveLikeHandler
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

fun Application.pollModule() {
    routing {
        route("/api") {

            authenticate {
                route("/poll") {

                    get("/feed") {
                        call.commonRespond(PollFeedHandler().handle())
                    }

                    get("/my") {
                        call.commonRespond(
                            PollsMyListHandler().handle(
                                user = call.getUser()
                            )
                        )
                    }

                    get("/history") {
                        call.commonRespond(
                            PollHistoryHandler().handle(
                                user = call.getUser()
                            )
                        )
                    }

                    post("/create") {
                        val createdPollDto = call.receive<CreatedPollDto>()
                        call.commonRespond(
                            PollCreateHandler().handle(
                                user = call.getUser(),
                                createdPollDto = createdPollDto
                            )
                        )
                    }

                    get("/get/{pollId}") {
                        call.commonRespond(
                            PollGetHandler().handle(
                                pollId = call.getPollId()
                            )
                        )
                    }

                    post("/edit/{pollId}") {
                        val pollDto = call.receive<CreatedPollDto>()
                        call.commonRespond(
                            PollEditHandler().handle(
                                user = call.getUser(),
                                pollId = call.getPollId(),
                                pollDto = pollDto
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

                    post("/vote/{pollId}") {
                        call.commonRespond(
                            PollVoteHandler().handle(
                                user = call.getUser(),
                                pollId = call.getPollId()
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

                    post("/like/{pollId}/add") {
                        call.commonRespond(
                            PollAddLikeHandler().handle(
                                user = call.getUser(),
                                pollId = call.getPollId()
                            )
                        )
                    }

                    post("/like/{pollId}/remove") {
                        call.commonRespond(
                            PollRemoveLikeHandler().handle(
                                user = call.getUser(),
                                pollId = call.getPollId()
                            )
                        )
                    }

                    post("/dislike/{pollId}/add") {
                        call.commonRespond(
                            PollAddDislikeHandler().handle(
                                user = call.getUser(),
                                pollId = call.getPollId()
                            )
                        )
                    }

                    post("/dislike/{pollId}/remove") {
                        call.commonRespond(
                            PollRemoveDislikeHandler().handle(
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