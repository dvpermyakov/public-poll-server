package com.public.poll.cache

import com.public.poll.dto.PollDto

interface Cache {
    fun putPoll(key: String, value: PollDto)
    fun getPoll(key: String): PollDto?
}