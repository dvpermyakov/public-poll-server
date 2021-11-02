package com.public.poll.client

object SearchUri {
    val HOST: String = System.getenv("ELASTIC_INSTANCE_URL")
    val AUTH_HEADER: String = System.getenv("ELASTIC_AUTH_BASIC_TOKEN")
}