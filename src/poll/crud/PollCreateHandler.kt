package com.public.poll.poll.crud

import com.public.poll.dao.PollAnswerDao
import com.public.poll.dao.PollDao
import com.public.poll.dao.UserDao
import com.public.poll.dto.CreatedPollDto
import com.public.poll.table.PollStatus
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class PollCreateHandler {

    fun handle(user: UserDao, pollDto: CreatedPollDto) {
        transaction {
            val pollEntity = PollDao.new {
                created = DateTime.now()
                updated = DateTime.now()
                owner = user
                status = PollStatus.CREATED
                question = pollDto.question
                participantsRequired = 10
            }
            pollDto.answers.forEach { answer ->
                PollAnswerDao.new {
                    poll = pollEntity
                    text = answer
                }
            }
        }
    }

}