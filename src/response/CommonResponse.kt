package com.public.poll.response

import com.public.poll.dto.Dto
import io.ktor.http.HttpStatusCode

data class CommonResponse(
    val status: HttpStatusCode,
    val dto: Dto? = null
)