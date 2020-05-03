package com.public.poll.handler.auth

import com.public.poll.dao.UserDao
import com.public.poll.dto.TokenDto
import com.public.poll.dto.UserDto
import com.public.poll.mapper.UserMapper
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

class SignUpHandler {

    fun handle(tokenDto: TokenDto): UserDto? {
        return transaction {
            val encodedToken = Base64.getEncoder().encodeToString(tokenDto.token.toByteArray())
            val tokenSlices = encodedToken.split(":")
            val nameFromToken = tokenSlices[0]
            val passFromToken = tokenSlices[1]
            val emailFromToken = tokenSlices[2]

            val userEntity = UserDao.new {
                created = DateTime.now()
                name = nameFromToken
                password = passFromToken
                email = emailFromToken
            }
            UserMapper().map(userEntity)
        }
    }
}