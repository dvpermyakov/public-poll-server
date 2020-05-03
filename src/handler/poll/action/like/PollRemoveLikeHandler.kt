package com.public.poll.handler.poll.action.like

import com.public.poll.dao.PollLikeDao
import com.public.poll.dao.UserDao
import com.public.poll.dto.ErrorDto
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import com.public.poll.table.PollLikeTable
import com.public.poll.utils.toUUID
import io.ktor.http.HttpStatusCode
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

class PollRemoveLikeHandler {

    fun handle(user: UserDao, pollId: String): CommonResponse {
        val pollUuid = try {
            pollId.toUUID()
        } catch (ex: Exception) {
            return ErrorDto("PollId is invalid").toResponse()
        }
        return transaction {
            val pollLikeEntity = PollLikeDao.find {
                (PollLikeTable.ownerId eq user.id) and (PollLikeTable.pollId eq pollUuid)
            }.firstOrNull()
            if (pollLikeEntity == null) {
                ErrorDto("Like for this poll wasn't found").toResponse()
            } else {
                pollLikeEntity.delete()
                CommonResponse(HttpStatusCode.OK)
            }
        }
    }
}