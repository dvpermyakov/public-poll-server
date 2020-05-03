package com.public.poll.handler.poll.action.dislike

import com.public.poll.dao.PollDao
import com.public.poll.dao.PollDislikeDao
import com.public.poll.dao.UserDao
import com.public.poll.dto.ErrorDto
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import com.public.poll.table.PollDislikeTable
import com.public.poll.table.PollStatus
import com.public.poll.utils.toUUID
import io.ktor.http.HttpStatusCode
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class PollAddDislikeHandler {

    fun handle(user: UserDao, pollId: String): CommonResponse {
        val pollUuid = try {
            pollId.toUUID()
        } catch (ex: Exception) {
            return ErrorDto("PollId is invalid").toResponse()
        }
        val dislikeCount = transaction {
            PollDislikeDao.count(Op.build {
                (PollDislikeTable.ownerId eq user.id) and (PollDislikeTable.pollId eq pollUuid)
            })
        }
        return if (dislikeCount == 0L) {
            transaction {
                val pollEntity = PollDao.findById(pollUuid)
                if (pollEntity != null) {
                    if (pollEntity.status != PollStatus.APPROVED) {
                        ErrorDto("Poll should have status APPROVED, not it is ${pollEntity.status}").toResponse()
                    } else {
                        PollDislikeDao.new {
                            created = DateTime.now()
                            poll = pollEntity
                            owner = user
                        }
                        CommonResponse(HttpStatusCode.Created)
                    }
                } else {
                    ErrorDto("Poll wasn't found").toResponse()
                }
            }
        } else {
            ErrorDto("You have already disliked this poll").toResponse()
        }
    }
}