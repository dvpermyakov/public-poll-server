package com.public.poll.module

import com.public.poll.handler.user.UploadAvatarHandler
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.routing.*
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

fun Application.userModule(kodein: Kodein) {
    routing {
        route("/api") {
            route("/user") {

                authenticate {
                    post("/avatar") {
                        call.receiveMultipart().forEachPart { part ->
                            if (part is PartData.FileItem) {
                                val handler by kodein.instance<UploadAvatarHandler>()
                                call.commonRespond(
                                    handler.handle(
                                        userDto = call.getUser(),
                                        inputStream = part.streamProvider(),
                                        fileName = part.originalFileName
                                    )
                                )
                            }
                            part.dispose()
                        }
                    }
                }
            }
        }
    }
}