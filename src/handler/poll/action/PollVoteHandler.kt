package com.public.poll.handler.poll.action

import com.public.poll.dto.ErrorDto
import com.public.poll.dto.UserDto
import com.public.poll.repositories.PollRepository
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import io.ktor.http.HttpStatusCode

class PollVoteHandler(
    private val pollRepository: PollRepository
) {

    fun handle(userDto: UserDto, pollId: String, answerId: String): CommonResponse {
        return when (val result = pollRepository.createVote(
            userDto = userDto,
            pollId = pollId,
            answerId = answerId
        )) {
            PollRepository.CreateVoteResult.Success -> {
                CommonResponse(HttpStatusCode.Created)
            }
            PollRepository.CreateVoteResult.WrongPollIdFormat -> {
                ErrorDto("PollId is invalid").toResponse()
            }
            PollRepository.CreateVoteResult.WrongAnswerIdFormat -> {
                ErrorDto("AnswerId is invalid").toResponse()
            }
            PollRepository.CreateVoteResult.PollNotFound -> {
                ErrorDto("Poll wasn't found").toResponse()
            }
            PollRepository.CreateVoteResult.AnswerNotFound -> {
                ErrorDto("Answer wasn't found").toResponse()
            }
            PollRepository.CreateVoteResult.UserAlreadyVoted -> {
                ErrorDto("You have already voted to this poll").toResponse()
            }
            is PollRepository.CreateVoteResult.WrongStatus -> {
                ErrorDto("Poll should have status ACTIVE, not it is ${result.status}").toResponse()
            }
        }
    }
}