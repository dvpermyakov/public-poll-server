package com.public.poll.repositories

import com.public.poll.producer.Producer

class PollBrokerRepositoryImpl(private val producer: Producer) : PollBrokerRepository {
    override fun addPoll(id: String, question: String) {
        producer.send(id)
    }
}