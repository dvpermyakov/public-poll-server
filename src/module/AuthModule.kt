package com.public.poll.module

import com.public.poll.dto.TokenDto
import com.public.poll.dto.UserDto
import com.public.poll.handler.auth.SignInHandler
import com.public.poll.handler.auth.SignUpHandler
import com.public.poll.repositories.UserRepository
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.Principal
import io.ktor.auth.basic
import io.ktor.request.receive
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

data class UserPrincipal(val user: UserDto) : Principal

fun Application.authModule(kodein: Kodein) {
    install(Authentication) {
        basic {
            realm = "own realm for basic"
            validate { credentials ->
                val userRepository by kodein.instance<UserRepository>()
                val userDto = userRepository.findUserByCredential(
                    UserRepository.Credentials(
                        name = credentials.name,
                        password = credentials.password
                    )
                )
                userDto?.let { UserPrincipal(it) }
            }
        }
    }

    routing {
        route("/api") {
            route("/auth") {

                post("/signup") {
                    val token = call.receive<TokenDto>()
                    val handler by kodein.instance<SignUpHandler>()
                    call.commonRespond(handler.handle(token))
                }

                post("/signin") {
                    val token = call.receive<TokenDto>()
                    val handler by kodein.instance<SignInHandler>()
                    call.commonRespond(handler.handle(token))
                }
            }
        }
    }
}