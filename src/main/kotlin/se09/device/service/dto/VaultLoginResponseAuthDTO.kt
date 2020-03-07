package se09.device.service.dto

import com.beust.klaxon.Json

data class VaultLoginResponseAuthDTO(
        @Json(name = "client_token")
        val token: String
)
