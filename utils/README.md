![](https://img.shields.io/badge/dynamic/xml?color=green&label=utils-release&query=%2F%2Fmetadata%2Fversioning%2Frelease&url=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Fio%2Fgithub%2Fcatchpig%2Fkmvvm%2Futils%2Fmaven-metadata.xml)
![](https://img.shields.io/badge/dynamic/xml?color=green&label=utils-snapshot&query=%2F%2Fmetadata%2Fversioning%2Flatest&url=https%3A%2F%2Fs01.oss.sonatype.org%2Fcontent%2Frepositories%2Fsnapshots%2Fio%2Fgithub%2Fcatchpig%2Fkmvvm%2Futils%2Fmaven-metadata.xml)

## ActivityExt

+ Activity.startKtActivity -  打开activity
+ Fragment.startKtActivity - 打开activity

## AnnotationExt

+ Any.annotation - 获取当前类上的注解

## CalendarExt

+ String.calendar - 字符串转Calendar时间
+ Calendar.string - Calendar转字符串
+ Calendar.year - 获取年
+ Calendar.month - 获取月份
+ Calendar.dayOfMonth - 获取月份的第几天
+ Calendar.hour - 获取小时
+ Calendar.minute - 获取分钟
+ Calendar.second - 获取秒

## CommonExt

+ Context.dp2px - dp转px
+ Context.px2dp - px转dp
+ View.dp2px - dp转px
+ View.px2dp - px转dp

## ContextExt

+ Context.installApk - 安装apk
+ Context.colorResToInt - ColorRes转ColorInt
+ Context.copyClipboard - 复制文本到剪切板

## DateExt

+ String.date - 字符串转时间Date
+ Date.format - 时间Date转字符串
+ Date.calendar - Date转Calendar
+ Date.year - 获取年
+ Date.month - 获取月份
+ Date.dayOfMonth - 获取月份的第几天
+ Date.hour - 获取xiaoshi
+ Date.minute - 获取分钟
+ Date.second - 获取秒

## EditTextExt

+ EditText.keepDecimalListener - 保留小数位数,并监听返回值
+ EditText.keepDecimal - 保留小数位数
+ EditText.hidePassword - 隐藏密码
+ EditText.showPassword - 显示密码

## EncryptionExt

+ String.md5 - md5加密
+ ByteArray.md5 - md5加密

## GsonExt

+ String.jsonToClass - json转实体
+ Any.jsonToString - 实体转字符串

## LogUtils

+ 日志工具类
+ 需在application的onCreate方式中调用LogUtils.init()方法

## LogExt(日志打印)

+ String.logv - Log.v的日志
+ String.logd - Log.d的日志
+ String.logi - Log.i的日志
+ String.logw - Log.w的日志
+ String.loge - Log.e的日志

## SnackbarExt

+ Snackbar.setTextColorRes - 设置文本颜色
+ Snackbar.setBackgroundResource - 设置背景色
+ Snackbar.setMargin - 设置外边框距

## StringExt

+ CharSequence?.isNotNull - 不为null
+ CharSequence?.isNumber - 是否为数字

## TextViewExt

+ TextView.setTextColorRes - 设置文本颜色

## TransformExt

+ Bitmap.toByteArray - Bitmap转ByteArray
+ ByteArray.toBitmap - ByteArray转Bitmap
+ Drawable.toBitmap - Drawable转Bitmap
+ Bitmap.toDrawable - Bitmap转Drawable
+ Drawable.toByteArray - Drawable转ByteArray
+ ByteArray.toDrawable - ByteArray转Drawable
