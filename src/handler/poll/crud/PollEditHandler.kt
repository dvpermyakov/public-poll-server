package com.public.poll.handler.poll.crud

import com.public.poll.dto.CreatedPollDto
import com.public.poll.dto.Dto
import com.public.poll.dto.ErrorDto
import com.public.poll.dto.UserDto
import com.public.poll.repositories.PollRepository
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse

class PollEditHandler(
    private val pollRepository: PollRepository
) {

    fun handle(userDto: UserDto, pollId: String, createdPollDto: CreatedPollDto): CommonResponse {
        val resultDto: Dto = when (val result = pollRepository.editPoll(
            userDto = userDto,
            pollId = pollId,
            createdPollDto = createdPollDto
        )) {
            is PollRepository.EditPollResult.Success -> {
                result.pollDto
            }
            PollRepository.EditPollResult.WrongIdFormat -> {
                ErrorDto("PollId is invalid")
            }
            PollRepository.EditPollResult.NotFound -> {
                ErrorDto("Poll wasn't found")
            }
            is PollRepository.EditPollResult.WrongStatus -> {
                ErrorDto("Poll should have CREATED status, but it is ${result.status}")
            }
            PollRepository.EditPollResult.NoAccess -> {
                ErrorDto("Only owner can change poll")
            }
        }
        return resultDto.toResponse()
    }
}