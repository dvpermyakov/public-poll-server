package com.public.poll.poll.list

import com.public.poll.dao.PollDao
import com.public.poll.dao.UserDao
import com.public.poll.dto.PollCollectionDto
import com.public.poll.mapper.PollMapper
import com.public.poll.table.PollTable
import org.jetbrains.exposed.sql.transactions.transaction

class PollHistoryHandler {

    fun handle(user: UserDao): PollCollectionDto {
        return transaction {
            PollCollectionDto(items = PollDao
                .find { PollTable.ownerId eq user.id }
                .map { pollEntity ->
                    PollMapper().map(pollEntity)
                }
            )
        }
    }

}