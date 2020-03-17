package se09.device.service.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.server.types.files.SystemFile
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import se09.device.service.services.DeviceService
import se09.device.service.ws.UserService
import javax.inject.Inject

@Controller("/devices")
class DeviceController {

    private val LOG: Logger = LoggerFactory.getLogger(DeviceController::class.java)

    @Inject
    private lateinit var deviceService: DeviceService

    @Inject
    private lateinit var userService: UserService

    @Get(produces = [MediaType.APPLICATION_JSON])
    fun createDevice(): HttpResponse<SystemFile> {
        LOG.warn("########### createDevice")
        val zipFile = this.deviceService.createDevice()
        return HttpResponse.created(zipFile)
    }

    @Post(value = "/{deviceId}", produces = [MediaType.APPLICATION_JSON])
    fun claimDevice(
            @PathVariable deviceId: String,
            @Header(value = "Authorization") authHeader: String
            //@Header(value = "X-User-Id") userId: String
    ): HttpResponse<Any> {
        val token = authHeader.substringAfter(" ")
        val userId = userService.getUserIdFromToken(token)
        val password = deviceService.claimUserDevice(userId, deviceId)
        LOG.warn("########### claimDevice $deviceId")
        val responseBody = mapOf(
                "password" to password
        )
        return HttpResponse.ok(responseBody)
    }

}
