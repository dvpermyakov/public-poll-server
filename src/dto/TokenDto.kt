package com.public.poll.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    val token: String
) : Dto