package com.public.poll.dto

import kotlinx.serialization.Serializable

@Serializable
data class AnswerDto(
    val id: String,
    val text: String
)