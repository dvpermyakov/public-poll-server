package com.public.poll.producer

interface Producer {
    fun send(value: String)
}