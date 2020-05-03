package com.public.poll.handler.auth

import com.public.poll.dao.UserDao
import com.public.poll.dto.ErrorDto
import com.public.poll.dto.TokenDto
import com.public.poll.mapper.UserMapper
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse
import com.public.poll.table.UserTable
import io.ktor.http.HttpStatusCode
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

class SignUpHandler {

    fun handle(tokenDto: TokenDto): CommonResponse {
        val decodedToken = try {
            String(Base64.getDecoder().decode(tokenDto.token))
        } catch (ignore: Exception) {
            return ErrorDto("Token can't be parsed").toResponse(HttpStatusCode.BadRequest)
        }
        val tokenSlices = decodedToken.split(":")
        if (tokenSlices.size < 3) {
            return ErrorDto("Token has wrong format").toResponse(HttpStatusCode.BadRequest)
        }
        val nameFromToken = tokenSlices[0]
        val passFromToken = tokenSlices[1]
        val emailFromToken = tokenSlices[2]

        return transaction {
            if (UserDao.find { UserTable.email eq emailFromToken }.empty()) {
                val userEntity = UserDao.new {
                    created = DateTime.now()
                    name = nameFromToken
                    password = passFromToken
                    email = emailFromToken
                }
                val userDto = UserMapper().map(userEntity)
                userDto.toResponse(HttpStatusCode.Created)
            } else {
                ErrorDto("User with $emailFromToken already exists").toResponse()
            }
        }
    }
}