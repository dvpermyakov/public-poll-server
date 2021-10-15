package com.public.poll.client

import io.ktor.client.*

interface HttpClientProvider {
    fun getClient(): HttpClient
}