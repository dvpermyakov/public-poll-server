package com.public.poll.poll

import com.public.poll.dao.PollDao
import com.public.poll.models.User
import org.jetbrains.exposed.sql.transactions.transaction

class ReportHandler {

    fun handle(user: User, pollId: String) {
        transaction {
            val poll = PollDao.findById(id.toInt())
            if (poll != null) {
                // todo: report here
            }
        }
    }
}