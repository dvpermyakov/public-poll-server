package com.public.poll.handler.poll.crud

import com.public.poll.dao.PollDao
import com.public.poll.dto.PollDto
import com.public.poll.mapper.PollMapper
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class PollGetHandler {

    fun handle(pollId: UUID): PollDto? {
        return transaction {
            PollDao.findById(pollId)?.let { pollEntity ->
                PollMapper().map(pollEntity)
            }
        }
    }

}