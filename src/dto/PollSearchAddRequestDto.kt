package com.public.poll.dto

import kotlinx.serialization.Serializable

@Serializable
data class PollSearchAddRequestDto(
    val id: String,
    val question: String
)