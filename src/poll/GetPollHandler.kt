package com.public.poll.poll

import com.public.poll.dao.PollDao
import com.public.poll.dto.PollDto
import org.jetbrains.exposed.sql.transactions.transaction

class GetPollHandler {

    fun handle(pollId: String): PollDto {
        return transaction {
            val poll = PollDao.findById(pollId.toInt())
            if (poll != null) {
                PollDto(
                    id = poll.id.value.toString(),
                    question = poll.question,
                    answers = poll.answers.split(";")
                )
            } else {
                throw IllegalArgumentException("Illegal")
            }
        }
    }

}