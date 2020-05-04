package com.public.poll.mapper

import com.public.poll.dao.UserDao
import com.public.poll.dto.UserDto

interface UserMapper {
    fun map(userEntity: UserDao): UserDto
}