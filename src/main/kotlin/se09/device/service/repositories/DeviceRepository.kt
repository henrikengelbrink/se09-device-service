package se09.device.service.repositories

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import se09.device.service.models.Device
import java.util.*

@Repository
interface DeviceRepository : CrudRepository<Device, UUID> {

}