package com.public.poll.handler.poll.action.dislike

import com.public.poll.dao.PollDislikeDao
import com.public.poll.dao.UserDao
import com.public.poll.dto.ErrorDto
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import com.public.poll.table.PollDislikeTable
import com.public.poll.utils.toUUID
import io.ktor.http.HttpStatusCode
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

class PollRemoveDislikeHandler {

    fun handle(user: UserDao, pollId: String): CommonResponse {
        val pollUuid = try {
            pollId.toUUID()
        } catch (ex: Exception) {
            return ErrorDto("PollId is invalid").toResponse()
        }
        return transaction {
            val pollDislikeEntity = PollDislikeDao.find {
                (PollDislikeTable.ownerId eq user.id) and (PollDislikeTable.pollId eq pollUuid)
            }.firstOrNull()
            if (pollDislikeEntity == null) {
                ErrorDto("Dislike for this poll wasn't found").toResponse()
            } else {
                pollDislikeEntity.delete()
                CommonResponse(HttpStatusCode.OK)
            }
        }
    }
}