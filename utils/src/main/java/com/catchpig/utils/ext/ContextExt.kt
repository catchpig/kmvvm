package com.catchpig.utils.ext

import android.content.Context
import android.content.Intent
import android.net.Uri

import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

/**
 * 安装apk
 * @receiver Context
 * @param apkPath String apk本地路径
 */
fun Context.installApk(apkPath: String){
    "apk本地路径:$apkPath".logi()
    var pFile = File(apkPath)
    val intent = Intent()
    intent.action = Intent.ACTION_VIEW
    val uri: Uri
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        uri = FileProvider.getUriForFile(this, "$packageName.fileProvider", pFile)
    } else {
        uri = Uri.fromFile(pFile)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    intent.setDataAndType(uri, "application/vnd.android.package-archive")
    startActivity(intent)
}