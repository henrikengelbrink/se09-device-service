package se09.device.service.repositories

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import se09.device.service.models.DeviceCertificate
import java.util.*

@Repository
interface DeviceCertificateRepository : CrudRepository<DeviceCertificate, UUID> {

}