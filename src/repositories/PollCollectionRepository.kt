package com.public.poll.repositories

import com.public.poll.dto.PollCollectionDto
import com.public.poll.dto.UserDto

interface PollCollectionRepository {
    fun getPollsByUser(userDto: UserDto): PollCollectionDto
    fun getFeed(): PollCollectionDto
}