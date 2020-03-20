package se09.device.service.services

import at.favre.lib.crypto.bcrypt.BCrypt
import io.micronaut.http.server.types.files.SystemFile
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import se09.device.service.dto.UserDeviceDTO
import se09.device.service.dto.VerneMQRegisterDTO
import se09.device.service.models.Device
import se09.device.service.models.DeviceCertificate
import se09.device.service.models.UserDevice
import se09.device.service.repositories.DeviceCertificateRepository
import se09.device.service.repositories.DeviceRepository
import se09.device.service.repositories.UserDeviceRepository
import se09.device.service.utils.RandomPassword
import se09.device.service.ws.VaultService
import java.io.File
import java.time.Instant
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
                deviceId = device.id
        )

        return SystemFile(zipFile).attach("${device.id}.zip")
    }

    fun claimUserDevice(userId: String, deviceId: String): UserDeviceDTO {
        LOG.warn("claimUserDevice")
        val userDevice = userDeviceRepository.findByDeviceIdAndUserIdAndDeletedAtIsNull(deviceId = deviceId, userId = userId)
        LOG.warn("userDevice ${userDevice?.id}")
        if (userDevice != null) {
            LOG.warn("userDevice ${userDevice.id} DELETE")
            userDevice.deletedAt = Instant.now()
            LOG.warn("userDevice ${userDevice.id} date set")
            userDeviceRepository.save(userDevice)
            LOG.warn("userDevice ${userDevice.id} DELETED !!!!")
        }
        LOG.warn("userDevice PREEEE")
        val password = RandomPassword.randomPassword(20)
        LOG.warn("userDevice $password")
        val hashedPassword: String = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        LOG.warn("hashedPassword $hashedPassword")
        val newUserDevice = UserDevice(
                userId = userId,
                deviceId = deviceId,
                hashedPassword = hashedPassword
        )
        LOG.warn("newUserDevice ${newUserDevice.id}")
        userDeviceRepository.save(newUserDevice)
        LOG.warn("newUserDevice saved")
        return UserDeviceDTO(
                userDeviceId = newUserDevice.id,
                password = password
        )
    }

    fun credentialsValid(dto: VerneMQRegisterDTO): Boolean {
        val deviceId = dto.username.substringBefore(".engelbrink.dev")
        LOG.info("deviceId $deviceId")
        LOG.info("client_id ${dto.client_id}")

        val userDeviceOptional = userDeviceRepository.findById(dto.client_id)
        if (!userDeviceOptional.isPresent) {
            return false
        }
        val userDevice = userDeviceOptional.get()
        LOG.info("userDevice.id ${userDevice.id}")
        LOG.info("userDevice.deviceId ${userDevice.deviceId}")
        LOG.info("userDevice.userId ${userDevice.userId}")
        LOG.info("userDevice.hashedPassword ${userDevice.hashedPassword}")
        if (userDevice.deviceId.toString() != deviceId) {
            return false
        }
        LOG.info("PRE ###")
        val result: BCrypt.Result = BCrypt.verifyer().verify(dto.password.toCharArray(), userDevice.hashedPassword)
        LOG.info("POST $result.verified")
        return result.verified
    }

}
