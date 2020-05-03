package com.public.poll.dto

import kotlinx.serialization.Serializable

@Serializable
data class PollDto(
    val id: Int,
    val question: String,
    val answers: List<String>
)