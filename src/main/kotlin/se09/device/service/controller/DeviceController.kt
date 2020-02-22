package se09.device.service.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.server.types.files.SystemFile
import se09.device.service.services.DeviceService
import javax.inject.Inject

@Controller("/devices")
class DeviceController {

    @Inject
    private lateinit var deviceService: DeviceService

    @Get(produces = [MediaType.APPLICATION_JSON])
    fun createDevice(): HttpResponse<SystemFile> {
        val zipFile = this.deviceService.createDevice()
        return HttpResponse.created(zipFile)
    }

}