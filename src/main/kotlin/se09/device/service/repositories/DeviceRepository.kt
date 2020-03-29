package se09.device.service.repositories

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import se09.device.service.models.Device
import se09.device.service.models.UserDevice
import java.util.*

@Repository
interface DeviceRepository : CrudRepository<Device, String> {

    fun findByUserIdAndDeletedAtIsNull(userId: UUID): List<UserDevice>

}
