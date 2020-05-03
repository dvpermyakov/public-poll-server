package com.public.poll.poll.crud

import com.public.poll.dao.PollAnswerDao
import com.public.poll.dao.PollDao
import com.public.poll.dto.CreatedPollDto
import com.public.poll.table.PollStatus
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

class PollEditHandler {

    fun handle(pollId: UUID, pollDto: CreatedPollDto): Boolean {
        return transaction {
            val pollEntity = requireNotNull(PollDao.findById(pollId))

            if (pollEntity.status == PollStatus.CREATED) {
                pollEntity.updated = DateTime.now()
                pollEntity.question = pollDto.question
                pollEntity.answers.forEach { answerEntity ->
                    answerEntity.delete()
                }
                pollDto.answers.forEach { answer ->
                    PollAnswerDao.new {
                        poll = pollEntity
                        text = answer
                    }
                }
                return@transaction true
            }

            return@transaction false
        }
    }

}