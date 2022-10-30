package com.catchpig.kmvvm.apk.view

import android.Manifest
import com.catchpig.annotation.StatusBar
import com.catchpig.annotation.Title
import com.catchpig.kmvvm.R
import com.catchpig.kmvvm.apk.viewmodel.InstallApkViewModel
import com.catchpig.kmvvm.databinding.ActivityInstallApkBinding
import com.catchpig.mvvm.base.activity.BaseVMActivity
import com.catchpig.utils.ext.logd
import com.tbruyelle.rxpermissions3.RxPermissions

/**
 *
 * @author catchpig
 * @date 2020/11/20 15:50
 */
@Title(R.string.download_install_apk)
@StatusBar
class InstallApkActivity : BaseVMActivity<ActivityInstallApkBinding, InstallApkViewModel>() {
    private val rxPermissions by lazy { RxPermissions(this) }

    companion object {
        private const val TAG = "InstallApkActivity"
    }

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
        viewModel.progressLiveData.observe(this, {
            bodyBinding.progressBar.progress = (it.readLength * 100 / it.totalCount).toInt()
        })
        viewModel.progressLiveData1.observe(this, {
            "${(it.readLength * 100 / it.countLength).toInt()}".logd(TAG)
            bodyBinding.progressBar1.progress = (it.readLength * 100 / it.countLength).toInt()
            bodyBinding.progressText.text = "${it.completeCount}/${it.totalCount}"
        })
    }

    override fun initFlow() {

    }
}