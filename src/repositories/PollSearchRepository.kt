package com.public.poll.repositories

import com.public.poll.dto.PollCollectionDto

interface PollSearchRepository {
    fun searchPoll(query: String): SearchPollResult
    fun addPollToSearch(id: String, question: String): Boolean

    sealed class SearchPollResult {
        data class Success(val pollCollectionDto: PollCollectionDto) : SearchPollResult()
        object ExternalError : SearchPollResult()
    }
}