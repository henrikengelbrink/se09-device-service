package se09.device.service.dto

import se09.device.service.models.ClientType

interface VerneMQEventDTO {
    val fullClientId: String
    val username: String

    val clientId: String
        get() {
            return fullClientId.substringAfter(":")
        }

    val clientType: ClientType
        get() {
            when(fullClientId.substringBefore(":")) {
                "device" -> return ClientType.DEVICE
                "user" -> return ClientType.USER
                else -> throw java.lang.Exception()
            }
        }

}