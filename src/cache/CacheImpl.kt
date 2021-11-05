package com.public.poll.cache

import com.public.poll.dto.PollDto
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import redis.clients.jedis.HostAndPort
import redis.clients.jedis.Jedis

class CacheImpl(private val logger: Logger) : Cache {

    override fun putPoll(key: String, value: PollDto) {
        val jedis = Jedis(HostAndPort(System.getenv("REDIS_IP_ADDRESS"), 6379))
        logger.info("ip address = ${System.getenv("REDIS_IP_ADDRESS")}")
        val json = Json.encodeToString(value)
        jedis.set(key, json)
    }

    override fun getPoll(key: String): PollDto? {
        val jedis = Jedis(HostAndPort(System.getenv("REDIS_IP_ADDRESS"), 6379))
        logger.info("ip address = ${System.getenv("REDIS_IP_ADDRESS")}")
        val json = jedis.get(key)
        return json?.let { Json.decodeFromString<PollDto>(it) }
    }
}