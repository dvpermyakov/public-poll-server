package com.public.poll.handler.poll.action

import com.public.poll.dao.PollDao
import com.public.poll.dao.PollEngagementDao
import com.public.poll.dao.UserDao
import com.public.poll.dto.ErrorDto
import com.public.poll.mapper.PollMapper
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import com.public.poll.table.PollEngagementTable
import io.ktor.http.HttpStatusCode
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class PollEngageHandler {

    private val pollMapper = PollMapper()

    fun handle(user: UserDao, pollId: String): CommonResponse {
        val pollUuid = try {
            pollMapper.map(pollId)
        } catch (ex: Exception) {
            return ErrorDto("PollId is invalid").toResponse()
        }
        val engagementCount = transaction {
            PollEngagementDao.count(Op.build {
                (PollEngagementTable.ownerId eq user.id) and (PollEngagementTable.pollId eq pollUuid)
            })
        }
        return if (engagementCount == 0L) {
            val pollEntity = transaction { PollDao.findById(pollUuid) }
            if (pollEntity != null) {
                PollEngagementDao.new {
                    created = DateTime.now()
                    poll = pollEntity
                    owner = user
                }
                CommonResponse(HttpStatusCode.Created)
            } else {
                ErrorDto("Poll wasn't found").toResponse()
            }
        } else {
            ErrorDto("You have already engaged to this poll").toResponse()
        }
    }
}