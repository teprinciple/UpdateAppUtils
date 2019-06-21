package util

import java.io.File
import java.io.IOException
import java.io.InputStream
import java.security.cert.Certificate
import java.util.ArrayList
import java.util.jar.JarEntry
import java.util.jar.JarFile

/**
 * desc:
 * time: 2019/6/21
 * @author yk
 */
object ApkUtils {

    /**
     * 从APK中读取签名
     */
    @Throws(IOException::class)
    fun getSignaturesFromApk(file: File): List<String> {
        val signatures = ArrayList<String>()
        val jarFile = JarFile(file)
        try {
            val je = jarFile.getJarEntry("AndroidManifest.xml")
            val readBuffer = ByteArray(8192)
            val certs = loadCertificates(jarFile, je, readBuffer)
            if (certs != null) {
                for (c in certs) {
                    val sig = SignCheckUtil.bytes2HexString(SignCheckUtil.hashTemplate(c.encoded, "MD5"))
                        .replace("(?<=[0-9A-F]{2})[0-9A-F]{2}".toRegex(), ":$0")
                    signatures.add(sig)
                }
            }
        } catch (ex: Exception) {
        }

        return signatures
    }

    /**
     * 加载签名
     */
    private fun loadCertificates(jarFile: JarFile, je: JarEntry?, readBuffer: ByteArray): Array<Certificate>? {
        try {
            val `is` = jarFile.getInputStream(je)
            while (`is`.read(readBuffer, 0, readBuffer.size) != -1) {
            }
            `is`.close()
            return je?.certificates
        } catch (e: IOException) {
        }

        return null
    }

}
