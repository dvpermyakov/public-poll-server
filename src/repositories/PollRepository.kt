package com.public.poll.repositories

import com.public.poll.dto.CreatedPollDto
import com.public.poll.dto.PollDto
import com.public.poll.dto.UserDto
import com.public.poll.table.PollStatus

interface PollRepository {
    fun createPoll(userDto: UserDto, createdPollDto: CreatedPollDto): PollDto
    fun getPoll(pollId: String): GetPollResult
    fun editPoll(userDto: UserDto, pollId: String, createdPollDto: CreatedPollDto): EditPollResult

    sealed class GetPollResult {
        data class Success(val pollDto: PollDto) : GetPollResult()
        object WrongIdFormat : GetPollResult()
        object NotFound : GetPollResult()
    }

    sealed class EditPollResult {
        data class Success(val pollDto: PollDto) : EditPollResult()
        object WrongIdFormat : EditPollResult()
        object NotFound : EditPollResult()
        data class WrongStatus(val status: PollStatus) : EditPollResult()
        object NoAccess : EditPollResult()
    }
}