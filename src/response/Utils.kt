package com.public.poll.response

import com.public.poll.dto.Dto
import com.public.poll.dto.ErrorDto
import io.ktor.http.HttpStatusCode

fun Dto.toResponse(status: HttpStatusCode = HttpStatusCode.OK): CommonResponse {
    return CommonResponse(
        status = status,
        dto = this
    )
}

fun ErrorDto.toResponse(status: HttpStatusCode = HttpStatusCode.BadRequest): CommonResponse {
    return CommonResponse(
        status = status,
        dto = this
    )
}