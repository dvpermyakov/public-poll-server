package com.public.poll.dto

import kotlinx.serialization.Serializable

@Serializable
data class PollSearchResultDto(
    val hits: HitsWrapperDto
)

@Serializable
data class HitsWrapperDto(
    val hits: List<HitDto>
)

@Serializable
data class HitDto(
    val fields: PollFoundDto
)

@Serializable
data class PollFoundDto(
    val id: List<String>
)