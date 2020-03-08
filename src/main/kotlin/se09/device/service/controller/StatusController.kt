package se09.device.service.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller
class StatusController {

    @Get("/live", produces = [MediaType.APPLICATION_JSON])
    fun live(): HttpResponse<Map<String, String>> {
        return HttpResponse.ok(mapOf(
                "status" to "LIVE"
        ))
    }

    @Get("/ready", produces = [MediaType.APPLICATION_JSON])
    fun ready(): HttpResponse<Map<String, String>> {
        return HttpResponse.ok(mapOf(
                "status" to "READY"
        ))
    }

}