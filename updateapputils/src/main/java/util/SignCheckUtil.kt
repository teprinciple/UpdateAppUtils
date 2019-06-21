package util

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.pm.Signature
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and


/**
 * desc: 应用签名检测工具
 * time: 2019/6/21
 * @author yk
 */
object SignCheckUtil {

    private val HEX_DIGITS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

    /**
     * 获取当前应用签名文件 MD5
     */
    fun getAppSignatureMD5(): String {
        return getAppSignatureHash(Utils.getApp().packageName, "MD5")
    }

    private fun getAppSignatureHash(packageName: String, algorithm: String): String {
        if (packageName.isEmpty()) return ""
        val signature = getAppSignature(packageName)
        return if (signature == null || signature.isEmpty()) {
            ""
        } else {
            bytes2HexString(hashTemplate(signature[0].toByteArray(), algorithm))
                .replace("(?<=[0-9A-F]{2})[0-9A-F]{2}".toRegex(), ":$0")
        }
    }

    private fun getAppSignature(packageName: String): Array<Signature>? {
        if (packageName.isEmpty()) return null
        return try {
            val pm = Utils.getApp().packageManager
            @SuppressLint("PackageManagerGetSignatures")
            val pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            pi?.signatures
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    @JvmStatic
     fun hashTemplate(data: ByteArray?, algorithm: String): ByteArray? {
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

    @JvmStatic
     fun bytes2HexString(bytes: ByteArray?): String {
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
}