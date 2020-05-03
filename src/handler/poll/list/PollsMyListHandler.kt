package com.public.poll.handler.poll.list

import com.public.poll.dao.PollDao
import com.public.poll.dao.UserDao
import com.public.poll.dto.PollCollectionDto
import com.public.poll.mapper.PollMapper
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import com.public.poll.table.PollTable
import org.jetbrains.exposed.sql.transactions.transaction

class PollsMyListHandler {

    fun handle(user: UserDao): CommonResponse {
        return transaction {
            PollCollectionDto(items = PollDao
                .find { PollTable.ownerId eq user.id }
                .sortedByDescending { PollTable.created }
                .map { pollEntity ->
                    PollMapper().map(pollEntity)
                }
            ).toResponse()
        }
    }

}