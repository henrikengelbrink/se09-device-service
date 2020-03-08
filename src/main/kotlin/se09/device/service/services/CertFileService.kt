package se09.device.service.services

import se09.device.service.dto.VaultCertResponseDataDTO
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Singleton

@Singleton
class CertFileService {

    fun compressCertsToZipFile(path: String, data: VaultCertResponseDataDTO, deviceId: UUID): File {
        saveCertFile(path, "client.crt", data.certificate + "\n")
        saveCertFile(path, "client.key", data.privateKey + "\n")

        val certFiles = listOf("$path/client.crt", "$path/client.key", "/vault/secrets/chain.crt ")
        return createZip(certFiles, path, deviceId.toString())
    }

    private fun createZip(files: List<String>, path: String, fileName: String): File {
        val zipPath = "$path/$fileName.zip"
        val fos = FileOutputStream(zipPath)
        val zipOut = ZipOutputStream(fos)
        for (srcFile in files) {
            val fileToZip = File(srcFile)
            val fis = FileInputStream(fileToZip)
            val zipEntry = ZipEntry(fileToZip.name)
            zipOut.putNextEntry(zipEntry)
            val bytes = ByteArray(1024)
            var length: Int
            while (fis.read(bytes).also { length = it } >= 0) {
                zipOut.write(bytes, 0, length)
            }
            fis.close()
        }
        zipOut.close()
        fos.close()

        return File(zipPath)
    }

    private fun saveCertFile(certId: String, fileName: String, content:String) {
        File("$certId/$fileName").writeText(content)
    }


}
