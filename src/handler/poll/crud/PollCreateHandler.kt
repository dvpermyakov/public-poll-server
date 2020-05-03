package com.public.poll.handler.poll.crud

import com.public.poll.dao.PollAnswerDao
import com.public.poll.dao.PollDao
import com.public.poll.dao.UserDao
import com.public.poll.dto.CreatedPollDto
import com.public.poll.mapper.PollMapper
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import com.public.poll.table.PollStatus
import io.ktor.http.HttpStatusCode
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class PollCreateHandler {

    fun handle(user: UserDao, createdPollDto: CreatedPollDto): CommonResponse {
        return transaction {
            val pollEntity = PollDao.new {
                created = DateTime.now()
                updated = DateTime.now()
                owner = user
                status = PollStatus.CREATED
                question = createdPollDto.question
                participantsRequired = 10
            }
            createdPollDto.answers.forEach { answer ->
                PollAnswerDao.new {
                    poll = pollEntity
                    text = answer
                }
            }
            val pollDto = PollMapper().map(pollEntity)
            pollDto.toResponse(HttpStatusCode.Created)
        }
    }

}