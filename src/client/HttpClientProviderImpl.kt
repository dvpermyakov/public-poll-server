package com.public.poll.client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*

class HttpClientProviderImpl : HttpClientProvider {

    override fun getClient(): HttpClient {
        return HttpClient(CIO) {
            install(Logging) {
                level = LogLevel.BODY
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer(
                    json = kotlinx.serialization.json.Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }
}