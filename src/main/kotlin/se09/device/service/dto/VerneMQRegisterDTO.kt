package se09.device.service.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class VerneMQRegisterDTO(
        @JsonProperty("client_id")
        override val username: String,

        @JsonProperty("username")
        override val fullClientId: String,

        val password: String
): VerneMQEventDTO
