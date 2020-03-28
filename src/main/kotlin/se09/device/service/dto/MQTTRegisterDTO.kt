package se09.device.service.dto

data class MQTTRegisterDTO(
        val username: String,
        val clientId: String,
        val password: String
)
