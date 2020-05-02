package com.public.poll.poll.create

import com.public.poll.dao.PollDao
import com.public.poll.dto.CreatedPollDto
import org.jetbrains.exposed.sql.transactions.transaction

class CreatePollHandler {

    fun handle(poll: CreatedPollDto) {
        transaction {
            PollDao.new {
                question = poll.question
                answers = poll.answers.joinToString(";")
            }
        }
    }

}