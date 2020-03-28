package se09.device.service.ws

import io.micronaut.context.annotation.Value
import io.micronaut.http.HttpRequest.POST
import io.micronaut.http.MediaType
import io.micronaut.http.client.RxHttpClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import se09.device.service.dto.AuthenticationSessionDTO
import se09.device.service.dto.CertResponseDTO
import se09.device.service.dto.CreateCertificateDTO
import se09.device.service.models.ClientType
import java.net.URL
import javax.inject.Singleton

@Singleton
class CertService {

    @Value("\${service.url.cert}")
    private lateinit var certServiceUrl: String

    private val LOG: Logger = LoggerFactory.getLogger(CertService::class.java)

    fun createCert(clientId: String, clientType: ClientType): CertResponseDTO {
        LOG.info("createCert")
        val httpClient = RxHttpClient.create(URL(certServiceUrl))
        val payload = CreateCertificateDTO(
                clientId = clientId,
                clientType = clientType
        )
        val response = httpClient.toBlocking().exchange(
                POST("/certificates", payload).contentType(MediaType.APPLICATION_JSON),
                CertResponseDTO::class.java
        )
        return response.body()!!
    }

}
