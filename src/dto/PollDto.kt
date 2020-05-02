package com.public.poll.dto

import kotlinx.serialization.Serializable

@Serializable
data class PollDto(
    val id: String,
    val question: String,
    val answers: List<String>
)