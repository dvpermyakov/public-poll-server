package com.public.poll.cache

import com.public.poll.dto.PollDto

class CacheImpl : Cache {
//    private val jedis = Jedis(HostAndPort("127.0.0.1", 6379))

    override fun putPoll(key: String, value: PollDto) {
//        val json = Json.encodeToString(value)
//        jedis.set(key, json)
    }

    override fun getPoll(key: String): PollDto? {
        return null
//        val json = jedis.get(key)
//        return json?.let { Json.decodeFromString<PollDto>(it) }
    }
}