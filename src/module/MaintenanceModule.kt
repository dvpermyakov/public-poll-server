package com.public.poll.module

import com.public.poll.handler.poll.action.PollApproveHandler
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

fun Application.maintenanceModule(kodein: Kodein) {
    routing {

        authenticate {
            route("/mt") {
                route("/poll") {

                    post("/approve/{pollId}") {
                        val handler by kodein.instance<PollApproveHandler>()
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