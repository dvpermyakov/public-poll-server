package com.public.poll.repositories

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig.*
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*

class PollBrokerRepositoryImpl : PollBrokerRepository {
    override fun addPoll(id: String, question: String) {
        val properties = Properties()
        properties["bootstrap.servers"] = "localhost:9092"
        properties[ACKS_CONFIG] = "all"
        properties[KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.qualifiedName
        properties[VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.qualifiedName
        KafkaProducer<String, String>(properties).use { producer ->
            val record = ProducerRecord("test", id, id)
            producer.send(record)
        }
    }
}