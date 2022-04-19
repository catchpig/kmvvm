package com.catchpig.mvvm.network.manager

import android.os.Environment
import com.catchpig.mvvm.manager.ContextManager
import com.catchpig.mvvm.network.interceptor.DownloadInterceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.util.concurrent.TimeUnit

open class BaseDownloadManager {
    companion object {
        /**
         * 连接超时时间(秒)
         */
        private const val CONNECT_TIMEOUT = 5L

        /**
         * 读取数据超时时间(分钟)
         */
        private const val READ_TIMEOUT = 10L
    }


    private val logInterceptor by lazy {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpLoggingInterceptor
    }

    protected val downloadInterceptor by lazy {
        DownloadInterceptor()
    }

    private var okHttpClient: OkHttpClient? = null

    protected fun getOkHttpClient(): OkHttpClient {
        if (okHttpClient == null) {
            okHttpClient = OkHttpClient
                .Builder()
                /**
                 * 连接超时时间5秒
                 */
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                /**
                 * 读取数据超时时间10分钟
                 */
                .readTimeout(READ_TIMEOUT, TimeUnit.MINUTES)
                .addInterceptor(logInterceptor)
                .addInterceptor(downloadInterceptor)
                .build()
        }
        return okHttpClient!!
    }

    /**
     * 生成文件的地址
     * @param downloadUrl String
     * @return String
     */
    protected fun localFileName(downloadUrl: String): String {
        val fileName = downloadUrl.replace("/", "").replace("\\", "")
        var cashDir = getDownloadFilePath()
        return "$cashDir/download/$fileName"
    }

    private fun getDownloadFilePath(): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            //有SD卡,拿到SD卡的/storage/sdcard0/Android/data/包名/cash目录
            ContextManager.context.externalCacheDir!!.absolutePath
        } else {
            //没有SD卡的,拿到/data/data/包名/cash目录
            ContextManager.context.cacheDir.absolutePath
        }
    }

    /**
     * 将下载的数据写入本地缓存
     * @param responseBody ResponseBody 下载的文件数据
     * @param fileName String 文件名称
     * @return String 本地路径
     * @throws IOException
     */
    @Throws(IOException::class)
    protected fun writeCache(responseBody: ResponseBody, fileName: String): String {
        var file = File(fileName)
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        } else if (file.exists()) {
            if (file.length() == responseBody.contentLength()) {
                return fileName
            } else {
                file.delete()
            }
        }
        var randomAccessFile = RandomAccessFile(file, "rwd")
        var fileChannel = randomAccessFile.channel
        var mappedByteBuffer =
            fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, responseBody.contentLength())
        var buffer = ByteArray(1024 * 8)
        var len: Int
        while (true) {
            len = responseBody.byteStream().read(buffer)
            if (len == -1) {
                break
            }
            mappedByteBuffer.put(buffer, 0, len)
        }
        responseBody.byteStream().close()
        fileChannel?.close()
        randomAccessFile?.close()
        return fileName
    }
}