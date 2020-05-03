package com.public.poll.poll.action.like

import com.public.poll.dao.PollLikeDao
import com.public.poll.dao.UserDao
import com.public.poll.table.PollLikeTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class PollRemoveLikeHandler {

    fun handle(user: UserDao, pollId: UUID) {
        return transaction {
            PollLikeDao
                .find {
                    (PollLikeTable.ownerId eq user.id) and (PollLikeTable.pollId eq pollId)
                }
                .forEach { likeEntity ->
                    likeEntity.delete()
                }
        }
    }
}