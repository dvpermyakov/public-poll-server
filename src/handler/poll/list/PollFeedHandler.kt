package com.public.poll.handler.poll.list

import com.public.poll.dao.PollDao
import com.public.poll.dto.PollCollectionDto
import com.public.poll.mapper.PollMapper
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import com.public.poll.table.PollStatus
import com.public.poll.table.PollTable
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction

class PollFeedHandler {

    fun handle(): CommonResponse {
        return transaction {
            PollCollectionDto(items = PollDao
                .find {
                    (PollTable.status eq PollStatus.APPROVED) or (PollTable.status eq PollStatus.ACTIVE)
                }
                .sortedByDescending { PollTable.created }
                .map { pollEntity ->
                    PollMapper().map(pollEntity)
                }
            ).toResponse()
        }
    }

}