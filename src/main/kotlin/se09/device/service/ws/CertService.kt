package se09.device.service.ws

import io.micronaut.context.annotation.Value
import io.micronaut.http.HttpRequest.POST
import io.micronaut.http.MediaType
import io.micronaut.http.client.RxHttpClient
import se09.device.service.dto.CertResponseDTO
import se09.device.service.dto.CreateCertificateDTO
import se09.device.service.models.ClientType
import java.net.URL
import javax.inject.Singleton

@Singleton
class CertService {

    @Value("\${service.url.cert}")
    private lateinit var certServiceUrl: String

    fun createCert(clientId: String, clientType: ClientType): CertResponseDTO {
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
