package se09.device.service.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class VerneMQRegisterDTO(
        @JsonProperty("client_id")
        override val fullClientId: String,

        override val username: String,
        val password: String
): VerneMQEventDTO
