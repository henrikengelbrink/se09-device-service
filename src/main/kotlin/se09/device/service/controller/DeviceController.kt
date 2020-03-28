package se09.device.service.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.server.types.files.SystemFile
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import se09.device.service.dto.DeviceListDTO
import se09.device.service.dto.MQTTRegisterDTO
import se09.device.service.dto.UserDeviceDTO
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

    @Get(value = "/new", produces = [MediaType.APPLICATION_JSON])
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
    ): HttpResponse<UserDeviceDTO> {
        LOG.warn("########### claimDevice $deviceId")
        val token = authHeader.substringAfter(" ")
        val userId = userService.getUserIdFromToken(token)
        if (userId != null) {
            val userDeviceDTO = deviceService.claimUserDevice(userId, deviceId)
            return HttpResponse.ok(userDeviceDTO)
        } else {
            return HttpResponse.unauthorized()
        }
    }

    @Get(produces = [MediaType.APPLICATION_JSON])
    fun getDevicesForUser(
            @Header(value = "Authorization") authHeader: String
    ): HttpResponse<List<DeviceListDTO>> {
        LOG.warn("########### getDevicesForUser")
        val token = authHeader.substringAfter(" ")
        val userId = userService.getUserIdFromToken(token)
        if (userId != null) {
            val devices = deviceService.getDevicesForUser(userId)
            return HttpResponse.ok(devices)
        } else {
            return HttpResponse.unauthorized()
        }
    }

    @Post(value = "/mqtt/auth", produces = [MediaType.APPLICATION_JSON])
    fun handleMQTTRegister(
            @Body body: MQTTRegisterDTO
    ): HttpResponse<UserDeviceDTO> {
        LOG.warn("########### handleMQTTRegister")
        val valid = deviceService.mqttLoginValid(body)
        return if(valid) HttpResponse.ok()
        else HttpResponse.unauthorized()
    }

}
