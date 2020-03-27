package se09.device.service.services

import at.favre.lib.crypto.bcrypt.BCrypt
import io.micronaut.http.server.types.files.SystemFile
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import se09.device.service.dto.DeviceListDTO
import se09.device.service.dto.UserDeviceDTO
import se09.device.service.dto.VerneMQRegisterDTO
import se09.device.service.models.Device
import se09.device.service.models.DeviceCertificate
import se09.device.service.models.DeviceStatus
import se09.device.service.models.UserDevice
import se09.device.service.repositories.DeviceCertificateRepository
import se09.device.service.repositories.DeviceRepository
import se09.device.service.repositories.UserDeviceRepository
import se09.device.service.utils.RandomPassword
import se09.device.service.ws.VaultService
import java.io.File
import java.time.Instant
import java.util.*
import javax.inject.Inject


class DeviceService {

    private val LOG: Logger = LoggerFactory.getLogger(DeviceService::class.java)

    @Inject
    private lateinit var deviceRepository: DeviceRepository

    @Inject
    private lateinit var userDeviceRepository: UserDeviceRepository

    @Inject
    private lateinit var deviceCertificateRepository: DeviceCertificateRepository

    @Inject
    private lateinit var vaultService: VaultService

    @Inject
    private lateinit var certFileService: CertFileService

    fun createDevice(): SystemFile {
        var device = Device()
        device = this.deviceRepository.save(device)
        val certDTO = vaultService.generateCertificate(device.id.toString())

        val path = "/tmp/${device.id}"
        val file = File(path)
        if (!file.exists()) {
            if (file.mkdir()) {
                println("Directory is created!")
            } else {
                println("Failed to create directory!")
            }
        }

        val deviceCert = DeviceCertificate(
                deviceId = device.id,
                requestId = certDTO.requestId,
                serialNumber = certDTO.data.serialNumber,
                expiration = certDTO.data.expiration
        )
        deviceCertificateRepository.save(deviceCert)
        val zipFile = certFileService.compressCertsToZipFile(
                path = path,
                data = certDTO.data,
                deviceId = device.id.toString()
        )

        return SystemFile(zipFile).attach("${device.id}.zip")
    }

    fun claimUserDevice(userId: String, deviceId: String): UserDeviceDTO {
        LOG.warn("claimUserDevice")
        val deviceUUID = UUID.fromString(deviceId)
        val userUUID = UUID.fromString(userId)
        val userDevice = userDeviceRepository.findByDeviceIdAndUserIdAndDeletedAtIsNull(deviceId = deviceUUID, userId = userUUID)
        if (userDevice != null) {
            if (userDevice.userId.toString() != userId) {
                throw Exception()
            } else {
                userDevice.deletedAt = Instant.now()
                userDevice.status = DeviceStatus.INVALID
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

    fun credentialsValid(dto: VerneMQRegisterDTO): Boolean {
        //val deviceId = dto.username.substringBefore(".engelbrink.dev")
        val clientUUID = UUID.fromString(dto.username)
        val userDeviceOptional = userDeviceRepository.findById(clientUUID)
        if (!userDeviceOptional.isPresent) {
            return false
        }
        val userDevice = userDeviceOptional.get()
        if (userDevice.deviceId.toString() != dto.client_id || userDevice.status != DeviceStatus.ACTIVE) {
            return false
        }
        val result: BCrypt.Result = BCrypt.verifyer().verify(dto.password.toCharArray(), userDevice.hashedPassword)
        return result.verified
    }

    fun getDevicesForUser(userId: String): List<DeviceListDTO> {
        val userUUID = UUID.fromString(userId)
        val devices = userDeviceRepository.findByUserIdAndStatus(userUUID, DeviceStatus.ACTIVE)
        val dtoList = mutableListOf<DeviceListDTO>()
        for (device in devices) {
            dtoList.add(DeviceListDTO(
                    deviceId = device.id.toString()
            ))
        }
        return dtoList.toList()
    }

}
