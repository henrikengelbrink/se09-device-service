package se09.device.service.models

import javax.persistence.*

@Entity
@Table(name = "user_devices")
class UserDevice(
        @Column(name = "device_id")
        val deviceId: String = "",

        @Column(name = "user_id")
        val userId: String = "",

        @Column(name = "hashed_password")
        val hashedPassword: String = ""
): BaseEntity() {
        @Enumerated(EnumType.STRING)
        val status: DeviceStatus = DeviceStatus.ACTIVE
}
