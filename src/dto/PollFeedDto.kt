package com.public.poll.dto

import kotlinx.serialization.Serializable

@Serializable
data class PollFeedDto(
    val items: List<PollDto>
)