package com.public.poll.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(
    val message: String
) : Dto