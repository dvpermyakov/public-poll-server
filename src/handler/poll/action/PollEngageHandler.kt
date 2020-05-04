package com.public.poll.handler.poll.action

import com.public.poll.dto.ErrorDto
import com.public.poll.dto.UserDto
import com.public.poll.repositories.PollRepository
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import io.ktor.http.HttpStatusCode

class PollEngageHandler(
    private val pollRepository: PollRepository
) {

    fun handle(userDto: UserDto, pollId: String): CommonResponse {
        return when (val result = pollRepository.createEngagement(
            userDto = userDto,
            pollId = pollId
        )) {
            PollRepository.CreateEngagementResult.Success -> {
                CommonResponse(HttpStatusCode.Created)
            }
            PollRepository.CreateEngagementResult.WrongIdFormat -> {
                ErrorDto("PollId is invalid").toResponse()
            }
            PollRepository.CreateEngagementResult.PollNotFound -> {
                ErrorDto("Poll wasn't found").toResponse()
            }
            PollRepository.CreateEngagementResult.OwnerCannotBeEngaged -> {
                ErrorDto("Owner can't be engaged to his own poll").toResponse()
            }
            PollRepository.CreateEngagementResult.UserAlreadyEngaged -> {
                ErrorDto("You have already engaged to this poll").toResponse()
            }
            is PollRepository.CreateEngagementResult.WrongStatus -> {
                ErrorDto("Poll should have status APPROVED, not it is ${result.status}").toResponse()
            }
        }
    }
}