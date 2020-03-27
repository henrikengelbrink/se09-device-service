package se09.device.service.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.server.types.files.SystemFile
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import se09.device.service.dto.VerneMQRegisterDTO
import se09.device.service.services.DeviceService
import javax.inject.Inject

@Controller("/vernemq")
class VerneMQController {

    private val LOG: Logger = LoggerFactory.getLogger(VerneMQController::class.java)

    @Inject
    private lateinit var deviceService: DeviceService

    @Post("/auth_on_register", produces = [MediaType.APPLICATION_JSON])
    fun authOnRegister(@Body dto: VerneMQRegisterDTO): HttpResponse<Map<String, Any>> {
        LOG.info("auth_on_register")
        if (deviceService.credentialsValid(dto)) {
            return HttpResponse.ok(
                    mapOf(
                            "result" to "ok"
                    )
            )
        } else {
            return HttpResponse.ok(
                    mapOf(
                            "result" to mapOf(
                                    "error" to "not_allowed")
                    )
            )
        }
    }

    @Post("/auth_on_subscribe", produces = [MediaType.APPLICATION_JSON])
    fun authOnSubscribe(@Body body: Map<String, Any>): Map<String, Any> {
        LOG.info("auth_on_subscribe -> $body")
        return mapOf("result" to "ok")
//        return mapOf(
//                "result" to mapOf(
//                    "error" to "not_allowed"
//                )
////                "topics" to listOf(
////                        mapOf(
////                                "topic" to "test",
////                                "qos" to 128
////                        )
////                )
//        )
    }

    @Post("/auth_on_publish", produces = [MediaType.APPLICATION_JSON])
    fun authOnPublish(@Body body: Map<String, Any>): Map<String, Any> {
        LOG.info("auth_on_publish -> $body")
        return mapOf("result" to "ok")
        //return mapOf("result" to mapOf("error" to "not_allowed"))
    }



}