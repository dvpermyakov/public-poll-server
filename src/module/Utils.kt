package com.public.poll.module

import com.public.poll.dao.UserDao
import io.ktor.application.ApplicationCall
import io.ktor.auth.authentication
import java.util.*

internal fun ApplicationCall.getUser(): UserDao {
    return requireNotNull(authentication.principal<UserPrincipal>()).user
}

internal fun ApplicationCall.getPollId(): UUID {
    return UUID.fromString(requireNotNull(parameters["pollId"]))
}