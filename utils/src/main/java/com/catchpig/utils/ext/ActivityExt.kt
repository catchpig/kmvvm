package com.catchpig.utils.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

inline fun <reified T : Activity> Context.startKtActivity(
    intent: Intent = Intent()
) {

    intent.setClass(this, T::class.java)
    startActivity(intent)
}

inline fun <reified T : Activity> Fragment.startKtActivity(
    intent: Intent = Intent()
) {
    activity?.let {
        intent.setClass(it, T::class.java)
        it.startActivity(intent)
    }
}

inline fun <reified T : Activity> Activity.startKtActivityForResult(
    requestCode: Int,
    intent: Intent = Intent()
) {
    intent.setClass(this, T::class.java)
    startActivityForResult(intent, requestCode)
}

inline fun <reified T : Activity> Fragment.startKtActivityForResult(
    requestCode: Int,
    intent: Intent = Intent()
) {
    activity?.let {
        intent.setClass(it, T::class.java)
        it.startActivityForResult(intent, requestCode)
    }
}

