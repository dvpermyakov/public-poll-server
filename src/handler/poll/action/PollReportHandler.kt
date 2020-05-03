package com.public.poll.handler.poll.action

import com.public.poll.dao.PollDao
import com.public.poll.dao.PollReportDao
import com.public.poll.dao.UserDao
import com.public.poll.table.PollReportTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

class PollReportHandler {

    fun handle(user: UserDao, pollId: UUID): Boolean {
        return transaction {
            val reportCount = PollReportDao.count(Op.build {
                (PollReportTable.ownerId eq user.id) and (PollReportTable.pollId eq pollId)
            })
            if (reportCount == 0L) {
                PollDao.findById(pollId)?.let { pollEntity ->
                    PollReportDao.new {
                        created = DateTime.now()
                        poll = pollEntity
                        owner = user
                    }
                    return@transaction true
                }
            }
            return@transaction false
        }
    }
}