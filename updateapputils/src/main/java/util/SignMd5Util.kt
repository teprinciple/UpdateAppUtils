package util

import android.content.pm.PackageManager
import android.content.pm.Signature
import extension.globalContext
import java.io.File
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.cert.Certificate
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import kotlin.experimental.and

/**
 * desc: 获取签名 md5
 * time: 2019/6/21
 * @author teprinciple
 */
internal object SignMd5Util {

    private val HEX_DIGITS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

    /**
     * 获取当前应用签名文件md5
     */
    fun getAppSignatureMD5(): String {
        val packageName = globalContext()?.packageName ?: ""
        if (packageName.isEmpty()) return ""
        val signature = getAppSignature(packageName)
        return if (signature == null || signature.isEmpty()) {
            ""
        } else {
            bytes2HexString(hashTemplate(signature[0].toByteArray(), "MD5"))
                .replace("(?<=[0-9A-F]{2})[0-9A-F]{2}".toRegex(), ":$0")
        }
    }

    /**
     * 获取未安装apk 签名文件md5
     */
    fun getSignMD5FromApk(file: File): String {
        val signatures = ArrayList<String>()
        val jarFile = JarFile(file)
        try {
            val je = jarFile.getJarEntry("AndroidManifest.xml")
            val readBuffer = ByteArray(8192)
            val certs = loadCertificates(jarFile, je, readBuffer)
            if (certs != null) {
                for (c in certs) {
                    val sig = bytes2HexString(hashTemplate(c.encoded, "MD5"))
                        .replace("(?<=[0-9A-F]{2})[0-9A-F]{2}".toRegex(), ":$0")
                    signatures.add(sig)
                }
            }
        } catch (ex: Exception) {
        }
        return signatures.getOrNull(0) ?: ""
    }

    private fun getAppSignature(packageName: String): Array<Signature>? {
        if (packageName.isEmpty()) return null
        return try {
            val pm = globalContext()?.packageManager
            val pi = pm?.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            pi?.signatures
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun hashTemplate(data: ByteArray?, algorithm: String): ByteArray? {
        if (data == null || data.isEmpty()) return null
        return try {
            val md = MessageDigest.getInstance(algorithm)
            md.update(data)
            md.digest()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            null
        }
    }

    private fun bytes2HexString(bytes: ByteArray?): String {
        if (bytes == null) return ""
        val len = bytes.size
        if (len <= 0) return ""
        val ret = CharArray(len shl 1)
        var i = 0
        var j = 0
        while (i < len) {
            ret[j++] = HEX_DIGITS[bytes[i].toInt().shr(4) and 0x0f]
            ret[j++] = HEX_DIGITS[(bytes[i] and 0x0f).toInt()]
            i++
        }
        return String(ret)
    }

    /**
     * 加载签名
     */
    private fun loadCertificates(jarFile: JarFile, je: JarEntry?, readBuffer: ByteArray): Array<Certificate>? {
        try {
            val inputStream = jarFile.getInputStream(je)
            while (inputStream.read(readBuffer, 0, readBuffer.size) != -1) {
            }
            inputStream.close()
            return je?.certificates
        } catch (e: IOException) {
        }
        return null
    }
}