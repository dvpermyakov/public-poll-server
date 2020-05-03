package com.public.poll.handler.poll.action

import com.public.poll.dao.PollDao
import com.public.poll.dao.PollReportDao
import com.public.poll.dao.UserDao
import com.public.poll.dto.ErrorDto
import com.public.poll.mapper.PollMapper
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import com.public.poll.table.PollReportTable
import io.ktor.http.HttpStatusCode
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class PollReportHandler {

    private val pollMapper = PollMapper()

    fun handle(user: UserDao, pollId: String): CommonResponse {
        val pollUuid = try {
            pollMapper.map(pollId)
        } catch (ex: Exception) {
            return ErrorDto("PollId is invalid").toResponse()
        }
        val reportCount = transaction {
            PollReportDao.count(Op.build {
                (PollReportTable.ownerId eq user.id) and (PollReportTable.pollId eq pollUuid)
            })
        }
        return if (reportCount == 0L) {
            val pollEntity = PollDao.findById(pollUuid)
            if (pollEntity != null) {
                PollReportDao.new {
                    created = DateTime.now()
                    poll = pollEntity
                    owner = user
                }
                CommonResponse(HttpStatusCode.Created)
            } else {
                ErrorDto("Poll wasn't found").toResponse()
            }
        } else {
            ErrorDto("You have already reported to this poll").toResponse()
        }
    }
}