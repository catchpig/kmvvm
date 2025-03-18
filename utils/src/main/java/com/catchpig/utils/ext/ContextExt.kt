package com.catchpig.utils.ext

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

private const val TAG = "ContextExt"

/**
 * 根据屏幕Id创建上下文
 * @receiver Context
 * @param displayId Int
 * @return Context
 */
fun Context.createDisplayContext(displayId: Int): Context {
    val displayManager = getSystemService(DisplayManager::class.java)
    val display = displayManager.getDisplay(displayId)
    return createDisplayContext(display);
}

/**
 * 安装apk
 * 需要添加android.permission.REQUEST_INSTALL_PACKAGES权限
 * @see android.Manifest.permission.REQUEST_INSTALL_PACKAGES
 * @receiver Context
 * @param apkPath String apk本地路径
 */
fun Context.installApk(apkPath: String) {
    "apk本地路径:$apkPath".logi(TAG)
    var pFile = File(apkPath)
    val intent = Intent()
    intent.action = Intent.ACTION_VIEW
    val uri: Uri
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
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
fun Context.colorResToInt(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
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

/**
 * 获取剪切板数据
 * @receiver Context
 * @return String
 */
fun Context.getClipboard(): String? {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = clipboardManager.primaryClip
    val value = clipData?.run {
        getItemAt(0).text.toString()
    }.also {
        null
    }
    return value
}

fun Context.layoutInflater(): LayoutInflater {
    return LayoutInflater.from(this);
}

/**
 * 读取assets下的文本
 * @receiver Context
 * @param fileName String
 * @return String
 */
fun Context.readTextFromAssets(fileName: String): String {
    return assets.open(fileName).bufferedReader().use {
        it.readText()
    }
}

/**
 * 获取版本号
 * @receiver Context
 * @return String
 */
fun Context.versionName(): String {
    val packageInfo = packageManager.getPackageInfo(packageName, 0)
    return packageInfo.versionName
}

/**
 * 获取版本号
 * @receiver Context
 * @return Long
 */
fun Context.versionCode(): Long {
    val packageInfo = packageManager.getPackageInfo(packageName, 0)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageInfo.longVersionCode
    } else {
        packageInfo.versionCode as Long
    }
}