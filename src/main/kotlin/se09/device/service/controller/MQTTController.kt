package se09.device.service.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import se09.device.service.dto.MQTTRegisterDTO
import se09.device.service.dto.MQTTTopicDTO
import se09.device.service.dto.UserDeviceDTO
import se09.device.service.services.DeviceService
import se09.device.service.ws.UserService
import javax.inject.Inject

@Controller("/mqtt")
class MQTTController {

    @Inject
    private lateinit var deviceService: DeviceService

    @Inject
    private lateinit var userService: UserService

    @Post(value = "/register", produces = [MediaType.APPLICATION_JSON])
    fun handleMQTTRegister(
            @Body body: MQTTRegisterDTO
    ): HttpResponse<UserDeviceDTO> {
        val valid = deviceService.mqttLoginValid(body)
        return if(valid) HttpResponse.ok()
        else HttpResponse.unauthorized()
    }

    @Post(value = "/user/topic", produces = [MediaType.APPLICATION_JSON])
    fun handleMQTTTopic(
            @Body body: MQTTTopicDTO
    ): HttpResponse<Any> {
        val userId = userService.getUserIdFromUserClientId(body.clientId)
        val devices = deviceService.getDevicesForUser(userId)
        var valid = false
        for (device in devices) {
            if (device.deviceId == body.topic) {
                valid = true
                break
            }
        }
        return if(valid) HttpResponse.ok()
        else HttpResponse.unauthorized()
    }

}
