package com.catchpig.kmvvm.apk.view

import com.catchpig.annotation.Title
import com.catchpig.kmvvm.R
import com.catchpig.kmvvm.apk.viewmodel.InstallApkViewModel
import com.catchpig.kmvvm.databinding.ActivityInstallApkBinding
import com.catchpig.mvvm.base.activity.BaseVMActivity
import com.catchpig.utils.ext.installApk
import com.catchpig.utils.ext.logd
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

/**
 *
 * @author catchpig
 * @date 2020/11/20 15:50
 */
@Title(R.string.download_install_apk)
class InstallApkActivity : BaseVMActivity<ActivityInstallApkBinding, InstallApkViewModel>() {
    private val rxPermissions by lazy { XXPermissions.with(this) }

    companion object {
        private const val TAG = "InstallApkActivity"
    }

    override fun initParam() {

    }

    override fun initView() {
        rxPermissions
            .permission(Permission.WRITE_EXTERNAL_STORAGE)
            .request { permissions, allGranted ->
                if (allGranted) {
                    viewModel.download()
                }
            }
    }

    override fun initFlow() {
        viewModel.progressLiveData.observe(this) {
            bodyBinding.progressBar.progress = (it.readLength * 100 / it.countLength).toInt()
        }
        viewModel.progressLiveData1.observe(this) {
            "${(it.readLength * 100 / it.countLength).toInt()}".logd(TAG)
            bodyBinding.progressBar1.progress = (it.readLength * 100 / it.countLength).toInt()
            bodyBinding.progressText.text = "${it.completeCount}/${it.totalCount}"
        }
        viewModel.installApkLiveData.observe(this) {
            installApk(it)
        }
    }
}