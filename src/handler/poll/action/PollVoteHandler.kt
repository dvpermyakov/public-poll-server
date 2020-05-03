package com.public.poll.handler.poll.action

import com.public.poll.dao.PollAnswerDao
import com.public.poll.dao.PollDao
import com.public.poll.dao.PollVoteDao
import com.public.poll.dao.UserDao
import com.public.poll.dto.ErrorDto
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import com.public.poll.table.PollStatus
import com.public.poll.table.PollVoteTable
import com.public.poll.utils.toUUID
import io.ktor.http.HttpStatusCode
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class PollVoteHandler {

    fun handle(user: UserDao, pollId: String, answerId: String): CommonResponse {
        val pollUuid = try {
            pollId.toUUID()
        } catch (ex: Exception) {
            return ErrorDto("PollId is invalid").toResponse()
        }
        val answerUuid = try {
            answerId.toUUID()
        } catch (ex: Exception) {
            return ErrorDto("AnswerId is invalid").toResponse()
        }
        val voteCount = transaction {
            PollVoteDao.count(Op.build {
                (PollVoteTable.ownerId eq user.id) and (PollVoteTable.pollId eq pollUuid)
            })
        }
        return if (voteCount == 0L) {
            transaction {
                val pollEntity = PollDao.findById(pollUuid)
                if (pollEntity != null) {
                    if (pollEntity.status != PollStatus.ACTIVE) {
                        ErrorDto("Poll should have status ACTIVE, not it is ${pollEntity.status}").toResponse()
                    } else {
                        val answerEntity = PollAnswerDao.findById(answerUuid)
                        if (answerEntity != null) {
                            PollVoteDao.new {
                                created = DateTime.now()
                                poll = pollEntity
                                owner = user
                                answer = answerEntity
                            }
                            CommonResponse(HttpStatusCode.Created)
                        } else {
                            ErrorDto("Answer for poll wasn't found").toResponse()
                        }
                    }
                } else {
                    ErrorDto("Poll wasn't found").toResponse()
                }
            }
        } else {
            ErrorDto("You have already voted to this poll").toResponse()
        }
    }
}