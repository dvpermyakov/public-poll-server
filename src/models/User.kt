package com.public.poll.models

import org.joda.time.DateTime
import java.util.*

data class User(
    val id: UUID,
    val created: DateTime,
    val name: String,
    val email: String
)