package com.public.poll.handler.auth

import com.public.poll.dao.UserDao
import com.public.poll.dto.TokenDto
import com.public.poll.dto.UserDto
import com.public.poll.mapper.UserMapper
import com.public.poll.table.UserTable
import org.jetbrains.exposed.sql.and
import java.util.*

class SignInHandler {

    fun handle(tokenDto: TokenDto): UserDto? {
        val encodedToken = Base64.getEncoder().encodeToString(tokenDto.token.toByteArray())
        val nameFromToken = encodedToken.split(":")[0]
        val passFromToken = encodedToken.split(":")[1]

        return UserDao.find {
            (UserTable.name eq nameFromToken) and (UserTable.password eq passFromToken)
        }.firstOrNull()?.let { userEntity ->
            UserMapper().map(userEntity)
        }
    }
}