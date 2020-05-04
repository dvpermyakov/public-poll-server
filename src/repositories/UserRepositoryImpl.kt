package com.public.poll.repositories

import com.public.poll.dao.UserDao
import com.public.poll.dto.UserDto
import com.public.poll.mapper.UserMapper
import com.public.poll.table.UserTable
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class UserRepositoryImpl(
    private val userMapper: UserMapper
) : UserRepository {

    override fun createUser(name: String, pass: String, email: String): UserDto {
        return userMapper.map(
            UserDao.new {
                this.created = DateTime.now()
                this.name = name
                this.password = pass
                this.email = email
            }
        )
    }

    override fun findUserByEmail(email: String): UserDto? {
        return transaction {
            val userEntity = UserDao.find { UserTable.email eq email }.firstOrNull()
            userEntity?.let(userMapper::map)
        }
    }

    override fun findUserByCredential(credentials: UserRepository.Credentials): UserDto? {
        return transaction {
            val userEntity = UserDao.find { UserTable.email eq credentials.name }.firstOrNull()
            if (userEntity != null && userEntity.password.contentEquals(credentials.password)) {
                userMapper.map(userEntity)
            } else {
                null
            }
        }
    }
}