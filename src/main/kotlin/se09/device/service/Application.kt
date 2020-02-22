package se09.device.service

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("se09.device.service")
                .mainClass(Application.javaClass)
                .start()
    }
}