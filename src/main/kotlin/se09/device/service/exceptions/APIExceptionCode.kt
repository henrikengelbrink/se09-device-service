package se09.device.service.exceptions

import io.micronaut.http.HttpStatus

enum class APIExceptionCode(val httpCode: HttpStatus) {
    CERT_DIR_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
}
