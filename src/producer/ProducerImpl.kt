package com.public.poll.producer

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*

class ProducerImpl : Producer {

    private val producer = getProducer()

    override fun send(value: String) {
        producer.use { producer ->
            val record = ProducerRecord("test", value, value)
            producer.send(record)
        }
    }

    private fun getProducer(): KafkaProducer<String, String> {
        val properties = Properties().apply {
            setProperty("bootstrap.servers", "localhost:9092")
            setProperty(ProducerConfig.ACKS_CONFIG, "all")
            setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.qualifiedName)
            setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.qualifiedName)
        }

        return KafkaProducer<String, String>(properties)
    }
}