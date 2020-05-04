package com.public.poll.handler.poll.crud

import com.public.poll.dto.CreatedPollDto
import com.public.poll.dto.UserDto
import com.public.poll.repositories.PollRepository
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import io.ktor.http.HttpStatusCode

class PollCreateHandler(
    private val pollRepository: PollRepository
) {

    fun handle(userDto: UserDto, createdPollDto: CreatedPollDto): CommonResponse {
        return pollRepository.createPoll(userDto, createdPollDto).toResponse(HttpStatusCode.Created)
    }

}