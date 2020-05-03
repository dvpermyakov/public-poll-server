package com.public.poll.utils

import java.util.*

fun String.toUUID(): UUID {
    return UUID.fromString(this)
}