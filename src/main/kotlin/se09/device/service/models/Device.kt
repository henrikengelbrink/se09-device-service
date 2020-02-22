package se09.device.service.models

import javax.persistence.*

@Entity
@Table(name = "devices")
class Device: BaseEntity() {
        @Enumerated(EnumType.STRING)
        val status: DeviceStatus = DeviceStatus.ACTIVE
}
