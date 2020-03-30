package se09.device.service.dto

data class AuthenticationSessionDTO(
    val subject: String,
    val extra: MutableMap<String, Any>?,
    val header: MutableMap<String, Any>
)
