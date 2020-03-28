package se09.device.service.dto

import se09.device.service.models.ClientType

data class CreateCertificateDTO(
    val clientId: String,
    val clientType: ClientType
)
