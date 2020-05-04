package com.public.poll.handler.poll.action

import com.public.poll.dto.ErrorDto
import com.public.poll.dto.UserDto
import com.public.poll.repositories.PollRepository
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import io.ktor.http.HttpStatusCode

class PollApproveHandler(
    private val pollRepository: PollRepository
) {
    fun handle(userDto: UserDto, pollId: String): CommonResponse {
        return when (val result = pollRepository.approvePoll(
            userDto = userDto,
            pollId = pollId
        )) {
            PollRepository.ApprovePollResult.Success -> {
                CommonResponse(HttpStatusCode.OK)
            }
            PollRepository.ApprovePollResult.WrongIdFormat -> {
                ErrorDto("PollId is invalid").toResponse()
            }
            PollRepository.ApprovePollResult.PollNotFound -> {
                ErrorDto("Poll wasn't found").toResponse()
            }
            PollRepository.ApprovePollResult.NoAccess -> {
                ErrorDto("You don't have enough permissions to approve polls").toResponse()
            }
            is PollRepository.ApprovePollResult.WrongStatus -> {
                ErrorDto("Poll should have status CREATED, not it is ${result.status}").toResponse()
            }
        }
    }
}