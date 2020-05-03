package com.public.poll.poll.action.like

import com.public.poll.dao.PollDao
import com.public.poll.dao.PollLikeDao
import com.public.poll.dao.UserDao
import com.public.poll.table.PollLikeTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

class PollAddLikeHandler {

    fun handle(user: UserDao, pollId: UUID): Boolean {
        return transaction {
            val likeCount = PollLikeDao.count(Op.build {
                (PollLikeTable.ownerId eq user.id) and (PollLikeTable.pollId eq pollId)
            })
            if (likeCount == 0L) {
                PollDao.findById(pollId)?.let { pollEntity ->
                    PollLikeDao.new {
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