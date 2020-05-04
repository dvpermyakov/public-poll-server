package com.public.poll.handler.poll.crud

import com.public.poll.dto.Dto
import com.public.poll.dto.ErrorDto
import com.public.poll.repositories.PollRepository
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse

class PollGetHandler(
    private val pollRepository: PollRepository
) {

    fun handle(pollId: String): CommonResponse {
        val resultDto: Dto = when (val result = pollRepository.getPoll(pollId)) {
            is PollRepository.GetPollResult.Success -> {
                result.pollDto
            }
            PollRepository.GetPollResult.WrongIdFormat -> {
                ErrorDto("PollId is invalid")
            }
            PollRepository.GetPollResult.NotFound -> {
                ErrorDto("Poll wasn't found")
            }
        }
        return resultDto.toResponse()
    }
}