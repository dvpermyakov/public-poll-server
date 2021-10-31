package com.public.poll.dto

import kotlinx.serialization.Serializable

@Serializable
data class PollSearchRequestDto(
    val query: QueryDto,
    val fields: List<String>,
    val _source: Boolean
) {
    companion object {
        fun create(query: String) = PollSearchRequestDto(
            query = QueryDto(
                match = MatchDto(query)
            ),
            fields = listOf("id"),
            _source = false
        )
    }
}

@Serializable
data class QueryDto(
    val match: MatchDto
)

@Serializable
data class MatchDto(
    val question: String
)