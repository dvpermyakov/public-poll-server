package com.public.poll.dto

import kotlinx.serialization.Serializable

@Serializable
data class PollCollectionDto(
    val items: List<PollDto>
)