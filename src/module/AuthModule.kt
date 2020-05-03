package com.public.poll.module

import com.public.poll.dao.UserDao
import com.public.poll.dto.TokenDto
import com.public.poll.handler.auth.SignInHandler
import com.public.poll.handler.auth.SignUpHandler
import com.public.poll.table.UserTable
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
import org.jetbrains.exposed.sql.transactions.transaction

data class UserPrincipal(val user: UserDao) : Principal

fun Application.authModule() {
    install(Authentication) {
        basic {
            realm = "own realm for basic"
            validate { credentials ->
                transaction {
                    val user = UserDao.find { UserTable.name eq credentials.name }.firstOrNull()
                    if (user != null && user.password.contentEquals(credentials.password)) {
                        UserPrincipal(user)
                    } else {
                        null
                    }
                }
            }
        }
    }

    routing {
        route("/api") {
            route("/auth") {

                post("/signup") {
                    val token = call.receive<TokenDto>()
                    call.commonRespond(SignUpHandler().handle(token))
                }

                post("/signin") {
                    val token = call.receive<TokenDto>()
                    call.commonRespond(SignInHandler().handle(token))
                }
            }
        }
    }
}