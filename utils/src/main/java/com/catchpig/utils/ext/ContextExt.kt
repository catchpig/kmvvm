package com.catchpig.utils.ext

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri

import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

private const val TAG = "ContextExt"
/**
 * 安装apk
 * @receiver Context
 * @param apkPath String apk本地路径
 */
fun Context.installApk(apkPath: String){
    "apk本地路径:$apkPath".logi(TAG)
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

/**
 * ColorRes 转 ColorInt
 */
@ColorInt
fun Context.colorResToInt(@ColorRes colorRes: Int):Int{
    return ContextCompat.getColor(this,colorRes)
}

/**
 * 复制文本到剪贴板
 */
fun Context.copyClipboard(charSequence: CharSequence) {
    val clipboardManager: ClipboardManager =
        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText(null, charSequence)
    clipboardManager.setPrimaryClip(clipData)
}