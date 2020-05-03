package com.public.poll.handler.poll.crud

import com.public.poll.dao.PollDao
import com.public.poll.dto.ErrorDto
import com.public.poll.mapper.PollMapper
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import org.jetbrains.exposed.sql.transactions.transaction

class PollGetHandler {

    private val pollMapper = PollMapper()

    fun handle(pollId: String): CommonResponse {
        val pollUuid = try {
            pollMapper.map(pollId)
        } catch (ex: Exception) {
            return ErrorDto("PollId is invalid").toResponse()
        }
        return transaction {
            val pollEntity = PollDao.findById(pollUuid)
            if (pollEntity == null) {
                ErrorDto("Poll wasn't found").toResponse()
            } else {
                pollMapper.map(pollEntity).toResponse()
            }
        }
    }
}