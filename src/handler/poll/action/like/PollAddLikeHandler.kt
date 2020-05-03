package com.public.poll.handler.poll.action.like

import com.public.poll.dao.PollDao
import com.public.poll.dao.PollLikeDao
import com.public.poll.dao.UserDao
import com.public.poll.dto.ErrorDto
import com.public.poll.mapper.PollMapper
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import com.public.poll.table.PollLikeTable
import io.ktor.http.HttpStatusCode
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class PollAddLikeHandler {

    private val pollMapper = PollMapper()

    fun handle(user: UserDao, pollId: String): CommonResponse {
        val pollUuid = try {
            pollMapper.map(pollId)
        } catch (ex: Exception) {
            return ErrorDto("PollId is invalid").toResponse()
        }
        val likeCount = transaction {
            PollLikeDao.count(Op.build {
                (PollLikeTable.ownerId eq user.id) and (PollLikeTable.pollId eq pollUuid)
            })
        }
        return if (likeCount == 0L) {
            val pollEntity = PollDao.findById(pollUuid)
            if (pollEntity != null) {
                PollLikeDao.new {
                    created = DateTime.now()
                    poll = pollEntity
                    owner = user
                }
                CommonResponse(HttpStatusCode.Created)
            } else {
                ErrorDto("Poll wasn't found").toResponse()
            }
        } else {
            ErrorDto("You have already liked this poll").toResponse()
        }
    }
}