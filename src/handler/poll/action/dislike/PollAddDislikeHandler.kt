package com.public.poll.handler.poll.action.dislike

import com.public.poll.dao.PollDao
import com.public.poll.dao.PollDislikeDao
import com.public.poll.dao.UserDao
import com.public.poll.dto.ErrorDto
import com.public.poll.mapper.PollMapper
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import com.public.poll.table.PollDislikeTable
import io.ktor.http.HttpStatusCode
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class PollAddDislikeHandler {

    private val pollMapper = PollMapper()

    fun handle(user: UserDao, pollId: String): CommonResponse {
        val pollUuid = try {
            pollMapper.map(pollId)
        } catch (ex: Exception) {
            return ErrorDto("PollId is invalid").toResponse()
        }
        val dislikeCount = transaction {
            PollDislikeDao.count(Op.build {
                (PollDislikeTable.ownerId eq user.id) and (PollDislikeTable.pollId eq pollUuid)
            })
        }
        return if (dislikeCount == 0L) {
            val pollEntity = PollDao.findById(pollUuid)
            if (pollEntity != null) {
                PollDislikeDao.new {
                    created = DateTime.now()
                    poll = pollEntity
                    owner = user
                }
                CommonResponse(HttpStatusCode.Created)
            } else {
                ErrorDto("Poll wasn't found").toResponse()
            }
        } else {
            ErrorDto("You have already disliked this poll").toResponse()
        }
    }
}