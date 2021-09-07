package com.catchpig.utils.ext

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

/**
 *
 * @author TLi2
 **/

fun Context.toast(content: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, content, duration).apply {
        show()
    }
}

fun Context.toast(@StringRes id: Int, duration: Int = Toast.LENGTH_SHORT) {
    toast(getString(id), duration)
}

fun Context.longToast(content: String) {
    toast(content, Toast.LENGTH_LONG)
}

fun Context.longToast(@StringRes id: Int) {
    toast(id, Toast.LENGTH_LONG)
}

fun Fragment.toast(content: String){
    this.activity?.toast(content)
}

fun Fragment.toast(@StringRes id:Int){
    this.activity?.toast(id)
}

fun Fragment.longToast(content: String){
    this.activity?.longToast(content)
}

fun Fragment.longToast(@StringRes id:Int){
    this.activity?.longToast(id)
}

fun String.toast(context: Context){
    context.toast(this)
}

fun String.longToast(context: Context){
    context.longToast(this)
}

fun Any.longToast(context: Context,content: String){
    content.longToast(context)
}

fun Any.longToast(context: Context,@StringRes id:Int){
    context.longToast(id)
}

fun Any.toast(context: Context,content: String){
    content.toast(context)
}

fun Any.toast(context: Context,@StringRes id:Int){
    context.toast(id)
}