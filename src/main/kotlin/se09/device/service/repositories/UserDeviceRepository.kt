package se09.device.service.repositories

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import se09.device.service.models.UserDevice
import java.util.*

@Repository
interface UserDeviceRepository : CrudRepository<UserDevice, String> {

    fun findByDeviceIdAndUserIdAndDeletedAtIsNull(deviceId: UUID, userId: UUID): UserDevice?

}
