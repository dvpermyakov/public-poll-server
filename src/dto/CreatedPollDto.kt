package com.public.poll.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreatedPollDto(
    val question: String,
    val answers: List<String>
) : Dto