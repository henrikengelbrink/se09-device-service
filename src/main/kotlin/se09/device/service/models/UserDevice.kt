package se09.device.service.models

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user_devices")
class UserDevice(
        @Column(name = "device_id")
        open var deviceId: UUID,

        @Column(name = "user_id")
        open var userId: UUID,

        @Column(name = "hashed_password")
        val hashedPassword: String = ""
): BaseEntity() {
        @Enumerated(EnumType.STRING)
        var status: DeviceStatus = DeviceStatus.ACTIVE
}
