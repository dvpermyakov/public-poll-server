package com.public.poll.mapper

import com.public.poll.dao.UserDao
import com.public.poll.dto.UserDto

class UserMapper {

    fun map(userEntity: UserDao): UserDto {
        return UserDto(
            id = userEntity.id.toString(),
            name = userEntity.name,
            email = userEntity.email
        )
    }

}