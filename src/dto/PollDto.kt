package com.public.poll.dto

import com.public.poll.table.PollStatus
import kotlinx.serialization.Serializable

@Serializable
data class PollDto(
    val id: String,
    val status: PollStatus,
    val question: String,
    val answers: List<String>,
    val participantRequired: Int,
    val likeCount: Long,
    val dislikeCount: Long
) : Dto