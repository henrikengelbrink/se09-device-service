package se09.device.service.models

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user_devices")
class UserDevice(
        @Column(name = "device_id")
        var deviceId: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000"),

        @Column(name = "user_id")
        var userId: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000"),

        @Column(name = "hashed_password")
        val hashedPassword: String = ""
): BaseEntity()
