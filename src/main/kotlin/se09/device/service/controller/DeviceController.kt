package se09.device.service.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Put
import io.micronaut.http.server.types.files.SystemFile
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import se09.device.service.services.DeviceService
import javax.inject.Inject

@Controller("/devices")
class DeviceController {

    private val LOG: Logger = LoggerFactory.getLogger(DeviceController::class.java)

    @Inject
    private lateinit var deviceService: DeviceService

    @Get(produces = [MediaType.APPLICATION_JSON])
    fun createDevice(): HttpResponse<SystemFile> {
        LOG.warn("########### createDevice")
        val zipFile = this.deviceService.createDevice()
        return HttpResponse.created(zipFile)
    }

    @Put(produces = [MediaType.APPLICATION_JSON])
    fun claimDevice(
            @Header(value = "X-User-Id") userId: String
    ): HttpResponse<Any> {
        LOG.warn("########### claimDevice $userId")
        val responseBody = mapOf(
                "password" to "1234567890"
        )
        return HttpResponse.ok(responseBody)
    }

}
