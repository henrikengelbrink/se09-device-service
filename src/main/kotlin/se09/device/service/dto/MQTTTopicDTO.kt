package se09.device.service.dto

data class MQTTTopicDTO(
        val topic: String,
        val qos: Int
)