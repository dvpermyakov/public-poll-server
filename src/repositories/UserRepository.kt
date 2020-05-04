package com.public.poll.repositories

import com.public.poll.dto.UserDto

interface UserRepository {
    fun createUser(name: String, pass: String, email: String): UserDto
    fun findUserByEmail(email: String): UserDto?
    fun findUserByCredential(credentials: Credentials): UserDto?

    data class Credentials(val name: String, val password: String)
}