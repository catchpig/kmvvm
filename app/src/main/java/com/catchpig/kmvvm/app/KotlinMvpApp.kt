package com.catchpig.kmvvm.app

import android.app.Application
import com.catchpig.kmvvm.BuildConfig
import com.catchpig.kmvvm.R
import com.catchpig.mvvm.manager.ContextManager
import com.catchpig.mvvm.network.manager.DownloadManager
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
        LogUtils.getInstance().init(applicationContext, BuildConfig.DEBUG)
        NetManager.getInstance().setDebug(BuildConfig.DEBUG)
        val downloadPath = "${ContextManager.getInstance().getContext().externalCacheDir!!.absolutePath}/kmvvmDownload"
        DownloadManager.setDownloadPath(downloadPath)
    }
}