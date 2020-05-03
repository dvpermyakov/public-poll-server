package com.public.poll.handler.poll.action.dislike

import com.public.poll.dao.PollDislikeDao
import com.public.poll.dao.UserDao
import com.public.poll.table.PollDislikeTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class PollRemoveDislikeHandler {

    fun handle(user: UserDao, pollId: UUID) {
        return transaction {
            PollDislikeDao
                .find {
                    (PollDislikeTable.ownerId eq user.id) and (PollDislikeTable.pollId eq pollId)
                }
                .forEach { likeEntity ->
                    likeEntity.delete()
                }
        }
    }
}