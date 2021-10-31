package com.public.poll.repositories

import com.public.poll.client.HttpClientProvider
import com.public.poll.client.SearchUri
import com.public.poll.dto.PollSearchAddRequestDto
import com.public.poll.dto.PollSearchAddResponseDto
import com.public.poll.dto.PollSearchRequestDto
import com.public.poll.dto.PollSearchResultDto
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

class PollSearchRepositoryImpl(
    private val pollCollectionRepository: PollCollectionRepository,
    private val httpClientProvider: HttpClientProvider
) : PollSearchRepository {

    override fun searchPoll(query: String): PollSearchRepository.SearchPollResult {
        return runBlocking {
            httpClientProvider.getClient().use { client ->
                try {
                    val response: PollSearchResultDto = client.request("${SearchUri.HOST}/$POLL_PATH/_search") {
                        method = HttpMethod.Get
                        contentType(ContentType.Application.Json)
                        body = PollSearchRequestDto.create(query)
                    }
                    val ids = response.hits.hits.flatMap { hit -> hit.fields.id }
                    PollSearchRepository.SearchPollResult.Success(
                        pollCollectionDto = pollCollectionRepository.getPollsByIds(ids)
                    )
                } catch (ex: Exception) {
                    println(ex.message)
                    PollSearchRepository.SearchPollResult.ExternalError
                }
            }
        }
    }

    override fun addPollToSearch(id: String, question: String): Boolean {
        return runBlocking {
            try {
                httpClientProvider.getClient().use { client ->
                    val response: PollSearchAddResponseDto = client.request("${SearchUri.HOST}/$POLL_PATH/_doc") {
                        method = HttpMethod.Post
                        contentType(ContentType.Application.Json)
                        body = PollSearchAddRequestDto(
                            id = id,
                            question = question
                        )
                    }
                    response._id.isNotEmpty()
                }
            } catch (ex: Exception) {
                println(ex.message)
                false
            }
        }
    }

    companion object {
        private const val POLL_PATH = "poll"
    }
}