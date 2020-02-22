package se09.device.service.ws

import com.beust.klaxon.Klaxon
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import se09.device.service.dto.VaultCertResponseDTO
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VaultService(
        @Client("http://localhost:8200")
        @Inject val httpClient: RxHttpClient
) {

    fun generateCertificate(clientId: String): VaultCertResponseDTO {

        val ttlSeconds = 2181246468 - Instant.now().epochSecond
        val hours = ttlSeconds / 3600

        val body = mapOf(
                "common_name" to "$clientId.engelbrink.dev",
                "ttl" to "${hours}h"
        )
        val request = HttpRequest.POST("/v1/pki_int/issue/engelbrink-dot-dev", body)
        request.headers.add("Content-Type", "application/json")
        request.headers.add("X-Vault-Token", "root123")
        val response = httpClient.toBlocking().retrieve(request)
        println(response)
        val jsonResponse = Klaxon().parse<VaultCertResponseDTO>(response)
        println(jsonResponse)
        return jsonResponse!!
    }

}
