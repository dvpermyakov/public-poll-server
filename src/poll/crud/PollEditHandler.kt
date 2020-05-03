package com.public.poll.poll.crud

import com.public.poll.dao.PollAnswerDao
import com.public.poll.dao.PollDao
import com.public.poll.dto.CreatedPollDto
import com.public.poll.table.PollAnswerTable
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class PollEditHandler {

    fun handle(pollId: UUID, pollDto: CreatedPollDto) {
        transaction {
            val pollEntity = requireNotNull(PollDao.findById(pollId))

            PollAnswerDao.find { PollAnswerTable.pollId eq pollId }.count()

//            pollDto.answers.forEach { answer ->
//                AnswerDao.new {
//                    text =
//                }
//            }
        }
    }

}