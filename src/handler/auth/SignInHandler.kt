package com.public.poll.handler.auth

import com.public.poll.dao.UserDao
import com.public.poll.dto.ErrorDto
import com.public.poll.dto.TokenDto
import com.public.poll.mapper.UserMapper
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import com.public.poll.table.UserTable
import io.ktor.http.HttpStatusCode
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class SignInHandler {

    fun handle(tokenDto: TokenDto): CommonResponse {
        val decodedToken = try {
            String(Base64.getDecoder().decode(tokenDto.token))
        } catch (ignore: Exception) {
            return ErrorDto("Token can't be parsed").toResponse(HttpStatusCode.BadRequest)
        }
        val tokenSlices = decodedToken.split(":")
        if (tokenSlices.size < 2) {
            return ErrorDto("Token has wrong format").toResponse(HttpStatusCode.BadRequest)
        }
        val nameFromToken = tokenSlices[0]
        val passFromToken = tokenSlices[1]

        return transaction {
            UserDao.find {
                (UserTable.name eq nameFromToken) and (UserTable.password eq passFromToken)
            }.firstOrNull()?.let { userEntity ->
                UserMapper().map(userEntity).toResponse()
            } ?: run {
                ErrorDto("Can't find user").toResponse()
            }
        }
    }
}