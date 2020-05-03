package com.public.poll.poll.action

import com.public.poll.dao.PollDao
import com.public.poll.dao.PollVoteDao
import com.public.poll.dao.UserDao
import com.public.poll.table.PollVoteTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

class PollVoteHandler {

    fun handle(user: UserDao, pollId: UUID): Boolean {
        return transaction {
            val voteCount = PollVoteDao.count(Op.build {
                (PollVoteTable.ownerId eq user.id) and (PollVoteTable.pollId eq pollId)
            })
            if (voteCount == 0L) {
                PollDao.findById(pollId)?.let { pollEntity ->
                    PollVoteDao.new {
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