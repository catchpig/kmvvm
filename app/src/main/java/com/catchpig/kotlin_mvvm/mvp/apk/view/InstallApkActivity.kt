package com.catchpig.kotlin_mvvm.mvp.apk.view

import android.Manifest
import com.catchpig.annotation.Title
import com.catchpig.kotlin_mvvm.R
import com.catchpig.kotlin_mvvm.databinding.ActivityInstallApkBinding
import com.catchpig.kotlin_mvvm.mvp.apk.viewmodel.InstallApkViewModel
import com.catchpig.mvvm.base.activity.BaseVMActivity
import com.tbruyelle.rxpermissions3.RxPermissions

/**
 *
 * @author catchpig
 * @date 2020/11/20 15:50
 */
@Title(R.string.download_install_apk)
class InstallApkActivity : BaseVMActivity<ActivityInstallApkBinding, InstallApkViewModel>() {
    private val rxPermissions by lazy { RxPermissions(this) }
    override fun initParam() {

    }

    override fun initView() {
        rxPermissions
            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe {
                if (it) {
                    viewModel.download()
                }
            }
    }

//    override fun setDownloadProgress(progress: Int) {
//        progressBar.progress = progress
//    }
//
//    override fun setDownloadProgress1(progress: Int) {
//        progressBar1.progress = progress
//    }
}