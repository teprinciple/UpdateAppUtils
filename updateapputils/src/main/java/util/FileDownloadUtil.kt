package util

import extension.log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_OK
import java.net.URL
import kotlin.concurrent.thread

/**
 * desc: 文件下载 当 FileDownloader 对某些apk下载失败时（比如：放在阿里云，码云上apk） 使用该工具类下载
 * time: 2019/8/28
 * @author yk
 */
object FileDownloadUtil {

    fun download(url: String, fileSavePath: String, fileName: String?) {
        thread {
            log("HttpURLConnection下载开始")
            var connection: HttpURLConnection? = null
            var outputStream: FileOutputStream? = null

            kotlin.runCatching {

                connection = URL(url).openConnection() as HttpURLConnection
                outputStream = FileOutputStream(File(fileSavePath, fileName))

                connection?.apply {
                    requestMethod = "GET"
                    setRequestProperty("Charset", "utf-8")
                    setRequestProperty("Accept-Encoding", "identity")
                    setRequestProperty("User-Agent", " Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36")
                    connect()
                }

                val responseCode = connection!!.responseCode
                if (responseCode == HTTP_OK) {
                    connection!!.inputStream.use { input ->
                        outputStream.use { output ->
                            input.copyToWithProgress(output!!) {
                                log("已下载：$it/${connection!!.contentLength}")
                            }
                        }
                    }
                }
            }.onSuccess {
                connection?.disconnect()
                outputStream?.close()
                log("HttpURLConnection下载完成")

            }.onFailure {
                connection?.disconnect()
                outputStream?.close()
                log("HttpURLConnection下载失败：${it.message}")
            }
        }
    }
}

fun InputStream.copyToWithProgress(out: OutputStream, bufferSize: Int = DEFAULT_BUFFER_SIZE, currentByte: (Long) -> Unit = {}): Long {
    var bytesCopied: Long = 0
    val buffer = ByteArray(bufferSize)
    var bytes = read(buffer)
    while (bytes >= 0) {
        out.write(buffer, 0, bytes)
        bytesCopied += bytes
        bytes = read(buffer)
        currentByte.invoke(bytesCopied)
    }
    return bytesCopied
}