package com.catchpig.download.response

import com.catchpig.download.callback.DownloadProgressListener
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

/**
 * 下载进度处理,并将进度下发给进度回调接口
 * @author catchpig
 * @date 2020/11/20 10:25
 */
class DownloadResponseBody(
    private val responseBody: ResponseBody,
    private val progressListener: DownloadProgressListener?
) : ResponseBody() {
    private var bufferedSource: BufferedSource? = null
    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = source(responseBody.source()).buffer()
        }
        return bufferedSource!!
    }

    /**
     *
     * @param source Source
     * @return Source?
     * @throws IOException
     */
    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L

            /**
             * 读取数据
             * @param sink Buffer
             * @param byteCount Long
             * @return Long
             * @throws IOException
             */
            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                //将下载进度下发给进度回调接口
                progressListener?.update(
                    totalBytesRead,
                    responseBody.contentLength(),
                    bytesRead == -1L
                )
                return bytesRead
            }
        }
    }

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }
}