package com.public.poll.repositories

interface PollBrokerRepository {
    fun addPoll(id: String, question: String)
}