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

    @Value("\${service.url.user}")
    private lateinit var userServiceUrl: String

    private val LOG: Logger = LoggerFactory.getLogger(UserService::class.java)

    fun getUserIdFromToken(token: String): String? {
        LOG.info("getUserIdFromToken")
        val httpClient = RxHttpClient.create(URL(userServiceUrl))
        val payload = AuthenticationSessionDTO(
                subject = token,
                extra = mutableMapOf(),
                header = mutableMapOf()
        )
        val response = httpClient.toBlocking().exchange(
                POST("/auth/hydrator", payload).contentType(MediaType.APPLICATION_JSON),
                AuthenticationSessionDTO::class.java
        )
        val responseDTO = response.body()!!
        return responseDTO.header.get("X-User-Id") as String?
    }

    fun getUserIdFromUserClientId(userClientId: String): String {
        LOG.info("getUserIdFromUserClientId")
        val httpClient = RxHttpClient.create(URL(userServiceUrl))
        val userId = httpClient.toBlocking().retrieve("/users/client/$userClientId}")
        LOG.info("getUserIdFromUserClientId $userId")
        return userId
    }

}
