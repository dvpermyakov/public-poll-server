package com.public.poll.handler.poll.list

import com.public.poll.dto.UserDto
import com.public.poll.repositories.PollCollectionRepository
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse

class PollsMyListHandler(
    private val pollCollectionRepository: PollCollectionRepository
) {

    fun handle(userDto: UserDto): CommonResponse {
        return pollCollectionRepository.getPollsByUser(userDto).toResponse()
    }

}