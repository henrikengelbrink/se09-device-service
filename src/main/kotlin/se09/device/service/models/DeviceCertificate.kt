package se09.device.service.models

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "device_certificates")
class DeviceCertificate(
        @Column(name = "device_id")
        open var deviceId: UUID,

        @Column(name = "request_id")
        val requestId: String = "",

        @Column(name = "serial_number")
        val serialNumber: String = "",

        val expiration: Float = 0.0F
): BaseEntity()
