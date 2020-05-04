package com.public.poll.handler.poll.list

import com.public.poll.repositories.PollCollectionRepository
import com.public.poll.response.CommonResponse
import com.public.poll.response.toResponse

class PollFeedHandler(
    private val pollCollectionRepository: PollCollectionRepository
) {

    fun handle(): CommonResponse {
        return pollCollectionRepository.getFeed().toResponse()
    }

}