package com.public.poll.repositories

import com.public.poll.dto.CreatedPollDto
import com.public.poll.dto.PollDto
import com.public.poll.dto.UserDto
import com.public.poll.table.PollStatus

interface PollRepository {
    fun createPoll(userDto: UserDto, createdPollDto: CreatedPollDto): PollDto
    fun getPoll(pollId: String): GetPollResult
    fun editPoll(userDto: UserDto, pollId: String, createdPollDto: CreatedPollDto): EditPollResult
    fun approvePoll(userDto: UserDto, pollId: String): ApprovePollResult

    fun createEngagement(userDto: UserDto, pollId: String): CreateEngagementResult
    fun createVote(userDto: UserDto, pollId: String, answerId: String): CreateVoteResult
    fun createReport(userDto: UserDto, pollId: String): CreateReportResult

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

    sealed class CreateEngagementResult {
        object Success : CreateEngagementResult()
        object WrongIdFormat : CreateEngagementResult()
        object PollNotFound : CreateEngagementResult()
        object OwnerCannotBeEngaged : CreateEngagementResult()
        object UserAlreadyEngaged : CreateEngagementResult()
        data class WrongStatus(val status: PollStatus) : CreateEngagementResult()
    }

    sealed class CreateVoteResult {
        object Success : CreateVoteResult()
        object WrongPollIdFormat : CreateVoteResult()
        object WrongAnswerIdFormat : CreateVoteResult()
        object PollNotFound : CreateVoteResult()
        object AnswerNotFound : CreateVoteResult()
        object UserAlreadyVoted : CreateVoteResult()
        data class WrongStatus(val status: PollStatus) : CreateVoteResult()
    }

    sealed class CreateReportResult {
        object Success : CreateReportResult()
        object WrongIdFormat : CreateReportResult()
        object PollNotFound : CreateReportResult()
        object UserAlreadyReported : CreateReportResult()
    }

    sealed class ApprovePollResult {
        object Success : ApprovePollResult()
        object WrongIdFormat : ApprovePollResult()
        object PollNotFound : ApprovePollResult()
        object NoAccess : ApprovePollResult()
        data class WrongStatus(val status: PollStatus) : ApprovePollResult()
    }
}