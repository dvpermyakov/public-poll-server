package com.public.poll.poll.action

import com.public.poll.dao.PollDao
import com.public.poll.dao.PollEngagementDao
import com.public.poll.dao.UserDao
import com.public.poll.table.PollEngagementTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

class PollEngageHandler {

    fun handle(user: UserDao, pollId: UUID): Boolean {
        return transaction {
            val engagementCount = PollEngagementDao.count(Op.build {
                (PollEngagementTable.ownerId eq user.id) and (PollEngagementTable.pollId eq pollId)
            })
            if (engagementCount == 0L) {
                PollDao.findById(pollId)?.let { pollEntity ->
                    PollEngagementDao.new {
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