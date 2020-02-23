package se09.device.service.exceptions

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import javax.inject.Singleton;

@Produces
@Singleton
@Requires(classes = [Exception::class, ExceptionHandler::class])
class OutOfStockExceptionHandler : ExceptionHandler<Exception?, HttpResponse<Any>> {

    override fun handle(request: HttpRequest<Any>, exception: Exception?): HttpResponse<Any> {
        println(exception)
        return HttpResponse.serverError()
    }

}