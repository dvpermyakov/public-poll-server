package com.public.poll.cache

import com.public.poll.dto.PollDto
import org.slf4j.Logger

class CacheImpl(private val logger: Logger) : Cache {
//    private val jedis = Jedis(HostAndPort(System.getenv("REDIS_IP_ADDRESS"), 6379))

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