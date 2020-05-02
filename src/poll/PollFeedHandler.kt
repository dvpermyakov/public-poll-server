package com.public.poll.poll

import com.public.poll.dao.PollDao
import com.public.poll.dto.PollDto
import com.public.poll.dto.PollFeedDto
import org.jetbrains.exposed.sql.transactions.transaction

class PollFeedHandler {

    fun handle(): PollFeedDto {
        return transaction {
            val items = PollDao.all().map { poll ->
                PollDto(
                    id = poll.id.value.toString(),
                    question = poll.question,
                    answers = poll.answers.split(";")
                )
            }
            PollFeedDto(items = items)
        }
    }

}