package se09.device.service.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.server.types.files.SystemFile
import se09.device.service.dto.DeviceListDTO
import se09.device.service.dto.UserDeviceDTO
import se09.device.service.services.DeviceService
import se09.device.service.ws.UserService
import javax.inject.Inject

@Controller("/devices")
class DeviceController {

    @Inject
    private lateinit var deviceService: DeviceService

    @Inject
    private lateinit var userService: UserService

    @Get(value = "/new", produces = [MediaType.APPLICATION_JSON])
    fun createDevice(): HttpResponse<SystemFile> {
        val zipFile = this.deviceService.createDevice()
        return HttpResponse.created(zipFile)
    }

    @Post(value = "/{deviceId}", produces = [MediaType.APPLICATION_JSON])
    fun claimDevice(
            @PathVariable deviceId: String,
            @Header(value = "Authorization") authHeader: String
            //@Header(value = "X-User-Id") userId: String
    ): HttpResponse<UserDeviceDTO> {
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
        val token = authHeader.substringAfter(" ")
        val userId = userService.getUserIdFromToken(token)
        if (userId != null) {
            val devices = deviceService.getDevicesForUser(userId)
            return HttpResponse.ok(devices)
        } else {
            return HttpResponse.unauthorized()
        }
    }

}
