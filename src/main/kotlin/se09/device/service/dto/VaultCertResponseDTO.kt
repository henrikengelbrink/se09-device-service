package se09.device.service.dto

import com.beust.klaxon.Json

data class VaultCertResponseDTO(
        @Json(name = "request_id")
        val requestId: String,

        val data: VaultCertResponseDataDTO
)
