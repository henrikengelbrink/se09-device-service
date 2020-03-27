package se09.device.service.dto

import se09.device.service.models.ClientType
import java.lang.Exception

interface VerneMQEventDTO {
    val username: String
    val fullClientId: String

    val clientId: String
        get() {
            return when(clientType) {
                ClientType.DEVICE -> fullClientId.substringBefore(".device")
                ClientType.USER -> fullClientId.substringBefore(".user")
            }
        }

    val clientType: ClientType
        get() {
            return when {
                fullClientId.contains("device") -> ClientType.DEVICE
                fullClientId.contains("user") -> ClientType.USER
                else -> {
                    throw Exception()
                }
            }
        }

}