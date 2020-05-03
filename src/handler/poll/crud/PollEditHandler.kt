package com.public.poll.handler.poll.crud

import com.public.poll.dao.PollAnswerDao
import com.public.poll.dao.PollDao
import com.public.poll.dao.UserDao
import com.public.poll.dto.CreatedPollDto
import com.public.poll.dto.ErrorDto
import com.public.poll.mapper.PollMapper
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import com.public.poll.table.PollStatus
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class PollEditHandler {

    private val pollMapper = PollMapper()

    fun handle(user: UserDao, pollId: String, pollDto: CreatedPollDto): CommonResponse {
        val pollUuid = try {
            pollMapper.map(pollId)
        } catch (ex: Exception) {
            return ErrorDto("PollId is invalid").toResponse()
        }

        return transaction {
            val pollEntity = PollDao.findById(pollUuid)
            when {
                pollEntity == null -> {
                    ErrorDto("PollId is invalid").toResponse()
                }
                pollEntity.owner.id != user.id -> {
                    ErrorDto("Only owner can change poll").toResponse()
                }
                pollEntity.status == PollStatus.CREATED -> {
                    transaction {
                        pollEntity.updated = DateTime.now()
                        pollEntity.question = pollDto.question
                        pollEntity.answers.forEach { answerEntity ->
                            answerEntity.delete()
                        }
                        pollDto.answers.forEach { answer ->
                            PollAnswerDao.new {
                                poll = pollEntity
                                text = answer
                            }
                        }
                        pollMapper.map(pollEntity).toResponse()
                    }
                }
                else -> {
                    ErrorDto(
                        "Poll should have CREATED status, but it is ${pollEntity.status}"
                    ).toResponse()
                }
            }
        }
    }
}