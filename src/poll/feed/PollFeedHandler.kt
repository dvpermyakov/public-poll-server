package com.public.poll.poll.feed

import com.public.poll.dao.PollDao
import com.public.poll.dto.PollDto
import com.public.poll.dto.PollFeedDto
import org.jetbrains.exposed.sql.transactions.transaction

class PollFeedHandler {

    fun handle(): PollFeedDto {
        return transaction {
            val items = PollDao.all().map { item ->
                PollDto(
                    id = item.id.value.toString(),
                    question = item.question,
                    answers = item.answers.split(";")
                )
            }
            PollFeedDto(items = items)
        }
    }

}