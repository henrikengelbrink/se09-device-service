package se09.device.service.services

import at.favre.lib.crypto.bcrypt.BCrypt
import io.micronaut.http.server.types.files.SystemFile
import se09.device.service.dto.*
import se09.device.service.exceptions.APIException
import se09.device.service.exceptions.APIExceptionCode
import se09.device.service.models.ClientType
import se09.device.service.models.Device
import se09.device.service.models.UserDevice
import se09.device.service.repositories.DeviceRepository
import se09.device.service.repositories.UserDeviceRepository
import se09.device.service.utils.RandomPassword
import se09.device.service.ws.CertService
import java.io.File
import java.time.Instant
import java.util.*
import javax.inject.Inject


class DeviceService {

    @Inject
    private lateinit var deviceRepository: DeviceRepository

    @Inject
    private lateinit var userDeviceRepository: UserDeviceRepository

    @Inject
    private lateinit var certService: CertService

    @Inject
    private lateinit var certFileService: CertFileService

    fun createDevice(): SystemFile {
        var device = Device()
        device = this.deviceRepository.save(device)
        val certDTO = certService.createCert(
                clientId = device.id.toString(),
                clientType = ClientType.DEVICE
        )
        val path = "/tmp/${device.id}"
        val file = File(path)
        if (!file.exists()) {
            if (!file.mkdir()) {
                throw APIException(APIExceptionCode.CERT_DIR_ERROR)
            }
        }
        val zipFile = certFileService.compressCertsToZipFile(
                path = path,
                certificate = certDTO.certificate,
                privateKey = certDTO.key,
                deviceId = device.id.toString()
        )

        return SystemFile(zipFile).attach("${device.id}.zip")
    }

    fun claimUserDevice(userId: String, deviceId: String): UserDeviceDTO {
        val deviceUUID = UUID.fromString(deviceId)
        val userUUID = UUID.fromString(userId)
        val userDevice = userDeviceRepository.findByDeviceIdAndUserIdAndDeletedAtIsNull(deviceId = deviceUUID, userId = userUUID)
        if (userDevice != null) {
            if (userDevice.userId.toString() != userId) {
                throw Exception()
            } else {
                userDevice.deletedAt = Instant.now()
                userDeviceRepository.update(userDevice)
            }
        }
        val password = RandomPassword.randomPassword(20)
        val hashedPassword: String = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        var newUserDevice = UserDevice(
                userId = userUUID,
                deviceId = deviceUUID,
                hashedPassword = hashedPassword
        )
        newUserDevice = userDeviceRepository.save(newUserDevice)
        return UserDeviceDTO(
                userDeviceId = newUserDevice.id.toString(),
                password = password
        )
    }

    fun getDevicesForUser(userId: String): List<DeviceListDTO> {
        val userUUID = UUID.fromString(userId)
        val devices = userDeviceRepository.findByUserIdAndDeletedAtIsNull(userUUID)
        val dtoList = mutableListOf<DeviceListDTO>()
        for (device in devices) {
            dtoList.add(DeviceListDTO(
                    deviceId = device.id.toString()
            ))
        }
        return dtoList.toList()
    }

    fun mqttLoginValid(dto: MQTTRegisterDTO): Boolean {
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

}
