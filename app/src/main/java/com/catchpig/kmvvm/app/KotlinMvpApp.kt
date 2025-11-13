package com.catchpig.kmvvm.app

import android.app.Application
import android.content.res.Configuration
import com.catchpig.download.manager.DownloadManager
import com.catchpig.kmvvm.R
import com.catchpig.utils.ext.logi
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
        initDownload()
    }

    /**
     * 不是必须要设置下载路径,内部有默认的下载路径,如果需要变下载路径,请重新设置
     */
    private fun initDownload() {
        val downloadPath = "${applicationContext.externalCacheDir!!.absolutePath}/kmvvmDownload"
        DownloadManager.setDownloadPath(downloadPath)
    }

    private var lastConfig: Configuration? = null
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (lastConfig != null) {
            val changed = newConfig.compareTo(lastConfig)
            "onConfigurationChanged->changed:$changed".logi(TAG)
        }
        lastConfig = newConfig
    }
}