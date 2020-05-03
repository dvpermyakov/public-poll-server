package com.public.poll.module

import com.public.poll.dao.UserDao
import com.public.poll.response.CommonResponse
import io.ktor.application.ApplicationCall
import io.ktor.auth.authentication
import io.ktor.response.respond
import java.util.*

internal suspend fun ApplicationCall.commonRespond(commonResponse: CommonResponse) {
    respond(commonResponse.status, commonResponse.dto)
}

internal fun ApplicationCall.getUser(): UserDao {
    return requireNotNull(authentication.principal<UserPrincipal>()).user
}

internal fun ApplicationCall.getPollId(): UUID {
    return UUID.fromString(requireNotNull(parameters["pollId"]))
}