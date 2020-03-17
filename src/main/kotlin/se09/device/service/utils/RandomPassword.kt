package se09.device.service.utils

import java.security.SecureRandom

class RandomPassword {

    companion object {

        private val charSource = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz#+-_/&%$§!()?"
        private val random = SecureRandom()

        fun randomPassword(length: Int): String {
            val stringBuilder = StringBuilder(length)
            repeat(length) {
                stringBuilder.append(charSource.get(random.nextInt(charSource.length)))
            }
            return stringBuilder.toString()
        }
    }

}
