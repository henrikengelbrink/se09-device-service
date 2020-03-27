package se09.device.service.repositories

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import se09.device.service.models.DeviceStatus
import se09.device.service.models.UserDevice
import java.util.*

@Repository
interface UserDeviceRepository : CrudRepository<UserDevice, UUID> {

    fun findByDeviceIdAndUserIdAndDeletedAtIsNull(deviceId: UUID, userId: UUID): UserDevice?
    fun update(entity: UserDevice): UserDevice?
    fun findByUserIdAndStatus(userId: UUID, status: DeviceStatus): List<UserDevice>

}
