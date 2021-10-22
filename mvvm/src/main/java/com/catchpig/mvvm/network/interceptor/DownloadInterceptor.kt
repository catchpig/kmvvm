package com.catchpig.mvvm.network.interceptor

import com.catchpig.mvvm.network.download.DownloadResponseBody
import com.catchpig.mvvm.listener.DownloadProgressListener
import io.reactivex.rxjava3.subscribers.ResourceSubscriber
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
/**
 * 下载拦截器
  * @author catchpig
 * @date 2020/11/20 10:25
 */
class DownloadInterceptor:Interceptor {
    private var downloadProgressListenerMap = mutableMapOf<String,DownloadProgressListener?>()
    fun addProgressListener(key: String, downloadProgressListener: DownloadProgressListener){
        downloadProgressListenerMap[key] = downloadProgressListener
        clearInvalidProgressListener()
    }

    /**
     * 清除无效的监听器
     */
    private fun clearInvalidProgressListener(){
        var iterator = downloadProgressListenerMap.iterator()
        iterator.forEach {
            val downloadProgressListener = it.value
            /**
             * 已经变成null的被清除
             */
            if (downloadProgressListener==null) {
                iterator.remove()
            }else if (downloadProgressListener is ResourceSubscriber<*>) {
                val resourceSubscriber = downloadProgressListener as ResourceSubscriber<*>
                /**
                 * 下载器已经执行完的被清除
                 */
                if (resourceSubscriber.isDisposed) {
                    iterator.remove()
                }
            }
        }
    }
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        val url = response.request.url.toUrl().toString()
        val downloadProgressListener = downloadProgressListenerMap[url]
        return response.newBuilder()
                .body(DownloadResponseBody(response.body!!, downloadProgressListener))
                .build()
    }
}