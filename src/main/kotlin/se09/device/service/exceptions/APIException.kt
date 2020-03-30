package se09.device.service.exceptions

class APIException(val code: APIExceptionCode) : RuntimeException()