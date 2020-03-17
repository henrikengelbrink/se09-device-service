package se09.device.service.services

import at.favre.lib.crypto.bcrypt.BCrypt
import io.micronaut.http.server.types.files.SystemFile
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
import java.util.*
import javax.inject.Inject


class DeviceService {

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

    fun claimUserDevice(userId: String, deviceId: String): String {
        val uuidUser = UUID.fromString(userId)
        val uuidDevice = UUID.fromString(deviceId)
        val userDevice = userDeviceRepository.findByDeviceIdAndUserIdAndDeletedAtIsNull(deviceId = uuidDevice, userId = uuidUser)
        if (userDevice != null) {
            userDevice.deletedAt = Instant.now()
            userDeviceRepository.save(userDevice)
        }
        val password = RandomPassword.randomPassword(20)
        val hashedPassword: String = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        val newUserDevice = UserDevice(
                userId = uuidUser,
                deviceId = uuidDevice,
                hashedPassword = hashedPassword
        )
        userDeviceRepository.save(newUserDevice)
        return password
    }

}
