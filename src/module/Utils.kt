package com.public.poll.module

import com.public.poll.dto.UserDto
import com.public.poll.response.CommonResponse
import io.ktor.application.ApplicationCall
import io.ktor.auth.authentication
import io.ktor.response.respond

internal suspend fun ApplicationCall.commonRespond(commonResponse: CommonResponse) {
    if (commonResponse.dto != null) {
        respond(commonResponse.status, commonResponse.dto)
    } else {
        respond(commonResponse.status)
    }
}

internal fun ApplicationCall.getUser(): UserDto {
    return requireNotNull(authentication.principal<UserPrincipal>()).user
}

internal fun ApplicationCall.getPollId(): String {
    return requireNotNull(parameters["pollId"])
}

internal fun ApplicationCall.getAnswerId(): String {
    return requireNotNull(parameters["answerId"])
}