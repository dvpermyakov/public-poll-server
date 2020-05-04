package com.public.poll.handler.poll.action

import com.public.poll.dto.ErrorDto
import com.public.poll.dto.UserDto
import com.public.poll.repositories.PollRepository
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import io.ktor.http.HttpStatusCode

class PollReportHandler(
    private val pollRepository: PollRepository
) {

    fun handle(userDto: UserDto, pollId: String): CommonResponse {

        return when (pollRepository.createReport(
            userDto = userDto,
            pollId = pollId
        )) {
            PollRepository.CreateReportResult.Success -> {
                CommonResponse(HttpStatusCode.Created)
            }
            PollRepository.CreateReportResult.WrongIdFormat -> {
                ErrorDto("PollId is invalid").toResponse()
            }
            PollRepository.CreateReportResult.PollNotFound -> {
                ErrorDto("Poll wasn't found").toResponse()
            }
            PollRepository.CreateReportResult.UserAlreadyReported -> {
                ErrorDto("You have already reported to this poll").toResponse()
            }
        }
    }
}