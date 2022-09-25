package com.catchpig.utils.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResult as ActivityResult

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

inline fun <reified T : Activity> AppCompatActivity.startKtActivity(
    intent: Intent = Intent(),
    crossinline callback: (ActivityResult) -> Unit
) {
    intent.setClass(this, T::class.java)
    registerForActivityResult(StartActivityForResult()) {
        callback(it)
    }.launch(intent)
}

inline fun <reified T : Activity> Fragment.startKtActivity(
    intent: Intent = Intent(),
    crossinline callback: (ActivityResult) -> Unit
) {
    activity?.let {
        intent.setClass(it, T::class.java)
        registerForActivityResult(StartActivityForResult()) { result ->
            callback(result)
        }.launch(intent)
    }
}

/**
 * startActivityForResult已经被废弃
 * @receiver Activity
 * @param requestCode Int
 * @param intent Intent
 */
@Deprecated(
    "当前方法已废弃,请使用startKtActivity",
    replaceWith = ReplaceWith(
        expression = "startKtActivity<T>(intent){\n}"
    )
)
inline fun <reified T : Activity> Activity.startKtActivityForResult(
    requestCode: Int,
    intent: Intent = Intent()
) {
    intent.setClass(this, T::class.java)
    startActivityForResult(intent, requestCode)
}

/**
 * startActivityForResult已经被废弃
 * @receiver Fragment
 * @param requestCode Int
 * @param intent Intent
 */
@Deprecated(
    "当前方法已废弃,请使用startKtActivity",
    replaceWith = ReplaceWith(
        expression = "startKtActivity<T>(intent){\n}",
    )
)
inline fun <reified T : Activity> Fragment.startKtActivityForResult(
    requestCode: Int,
    intent: Intent = Intent()
) {
    activity?.let {
        intent.setClass(it, T::class.java)
        it.startActivityForResult(intent, requestCode)
    }
}

