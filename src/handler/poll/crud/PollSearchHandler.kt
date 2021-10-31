package com.public.poll.handler.poll.crud

import com.public.poll.dto.ErrorDto
import com.public.poll.repositories.PollSearchRepository
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse

class PollSearchHandler(
    private val pollSearchRepository: PollSearchRepository
) {
    fun handle(query: String): CommonResponse {
        return when (val result = pollSearchRepository.searchPoll(query)) {
            is PollSearchRepository.SearchPollResult.Success -> {
                result.pollCollectionDto.toResponse()
            }
            PollSearchRepository.SearchPollResult.ExternalError -> {
                ErrorDto("Can't search with query = $query").toResponse()
            }
        }
    }
}