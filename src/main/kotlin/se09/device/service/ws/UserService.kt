package se09.device.service.ws

import io.micronaut.context.annotation.Value
import io.micronaut.http.HttpRequest.POST
import io.micronaut.http.MediaType
import io.micronaut.http.client.RxHttpClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import se09.device.service.dto.AuthenticationSessionDTO
import java.net.URL
import javax.inject.Singleton

@Singleton
class UserService {

    @Value("\${user.service.url}")
    private lateinit var userServiceUrl: String

    private val LOG: Logger = LoggerFactory.getLogger(UserService::class.java)

    fun getUserIdFromToken(token: String): String {
        LOG.info("getUserIdFromToken")
        val httpClient = RxHttpClient.create(URL(userServiceUrl))
        LOG.info("getUserIdFromToken 111")
        val payload = AuthenticationSessionDTO(
                subject = token,
                extra = mutableMapOf(),
                header = mutableMapOf()
        )
        LOG.info("getUserIdFromToken 222")
        val response = httpClient.toBlocking().exchange(
                POST("/auth/hydrator", payload).contentType(MediaType.APPLICATION_JSON),
                AuthenticationSessionDTO::class.java
        )
        LOG.info("getUserIdFromToken 333")
        val responseDTO = response.body()!!
        LOG.info("getUserIdFromToken 444")
        return responseDTO.header.get(" X-User-Id") as String
    }

}
