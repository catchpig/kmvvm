package com.catchpig.kmvvm.app

import android.app.Application
import com.catchpig.download.manager.DownloadManager
import com.catchpig.kmvvm.BuildConfig
import com.catchpig.kmvvm.R
import com.catchpig.mvvm.network.manager.NetManager
import com.catchpig.utils.LogUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @author catchpig
 * @date 2019/8/18 00:18
 */
class KotlinMvpApp : Application() {
    companion object {
        private const val TAG = "KotlinMvpApp"
    }

    init {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout -> //全局设置主题颜色
            layout.setPrimaryColorsId(R.color.c_333)
            MaterialHeader(context)
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context).setDrawableSize(20f)
        }
    }

    override fun onCreate() {
        super.onCreate()
        initLogger()
        initDownload()
    }

    private fun initLogger() {
        val path = "${externalCacheDir?.absolutePath}/logs"
        LogUtils.init("kmvvm", path)
    }

    /**
     * 不是必须要设置下载路径,内部有默认的下载路径,如果需要变下载路径,请重新设置
     */
    private fun initDownload() {
        val downloadPath = "${applicationContext.externalCacheDir!!.absolutePath}/kmvvmDownload"
        DownloadManager.setDownloadPath(downloadPath)
    }
}