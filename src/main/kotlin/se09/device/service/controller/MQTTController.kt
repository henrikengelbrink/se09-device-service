package se09.device.service.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import se09.device.service.dto.MQTTRegisterDTO
import se09.device.service.dto.MQTTTopicDTO
import se09.device.service.dto.UserDeviceDTO
import se09.device.service.services.DeviceService
import se09.device.service.ws.UserService
import javax.inject.Inject

@Controller("/mqtt")
class MQTTController {

    private val LOG: Logger = LoggerFactory.getLogger(MQTTController::class.java)

    @Inject
    private lateinit var deviceService: DeviceService

    @Inject
    private lateinit var userService: UserService

    @Post(value = "/register", produces = [MediaType.APPLICATION_JSON])
    fun handleMQTTRegister(
            @Body body: MQTTRegisterDTO
    ): HttpResponse<UserDeviceDTO> {
        LOG.warn("########### handleMQTTRegister")
        val valid = deviceService.mqttLoginValid(body)
        return if(valid) HttpResponse.ok()
        else HttpResponse.unauthorized()
    }

    @Post(value = "/user/topic", produces = [MediaType.APPLICATION_JSON])
    fun handleMQTTTopic(
            @Body body: MQTTTopicDTO
    ): HttpResponse<Any> {
        LOG.warn("########### handleMQTTTopic")
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
