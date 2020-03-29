package se09.device.service.dto

data class MQTTTopicDTO(
        val username: String,
        val clientId: String,
        val topic: String
)
