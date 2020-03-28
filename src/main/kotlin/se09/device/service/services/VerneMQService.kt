package se09.device.service.services

import at.favre.lib.crypto.bcrypt.BCrypt
import se09.device.service.dto.*
import se09.device.service.models.ClientType
import se09.device.service.repositories.UserDeviceRepository
import se09.device.service.ws.UserService
import java.util.*
import javax.inject.Inject


class VerneMQService {

    @Inject
    private lateinit var userDeviceRepository: UserDeviceRepository

    @Inject
    private lateinit var userService: UserService

    fun credentialsValid(dto: VerneMQRegisterDTO): Boolean {
        return when(dto.clientType) {
            ClientType.DEVICE -> deviceCredentialsValid(dto)
            ClientType.USER -> userCredentialsValid(dto)
        }
    }

    private fun deviceCredentialsValid(dto: VerneMQRegisterDTO): Boolean {
        val clientUUID = UUID.fromString(dto.username)
        val userDeviceOptional = userDeviceRepository.findById(clientUUID)
        if (!userDeviceOptional.isPresent) {
            return false
        }
        val userDevice = userDeviceOptional.get()
        if (userDevice.deviceId.toString() != dto.clientId || userDevice.deletedAt != null) {
            return false
        }
        val result: BCrypt.Result = BCrypt.verifyer().verify(dto.password.toCharArray(), userDevice.hashedPassword)
        return result.verified
    }

    private fun userCredentialsValid(dto: VerneMQRegisterDTO): Boolean {
        val userId = userService.getUserIdFromToken(dto.password)
        return userId != null && userId == dto.clientId
    }

    fun isAllowedToSubscribe(dto: VerneMQSubscribeDTO): Boolean {
        return when(dto.clientType) {
            ClientType.DEVICE -> deviceIsAllowedToSubscribe(dto)
            ClientType.USER -> userIsAllowedToSubscribe(dto)
        }
    }

    private fun deviceIsAllowedToSubscribe(dto: VerneMQSubscribeDTO): Boolean {
        // Todo
        return true
    }

    private fun userIsAllowedToSubscribe(dto: VerneMQSubscribeDTO): Boolean {
        // Todo
        return true
    }

}
