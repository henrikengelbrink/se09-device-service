package se09.device.service.dto

import com.beust.klaxon.Json

data class VaultCertResponseDataDTO(
        val certificate: String,

        @Json(name = "private_key")
        val privateKey: String,

        @Json(name = "serial_number")
        val serialNumber: String,

        val expiration: Float
)
