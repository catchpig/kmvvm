package com.catchpig.utils.ext

import android.content.Context
import android.widget.Toast

/**
 * 弹出LENGTH_LONG的toast
 * @receiver Context
 * @param stringRes Int
 */
fun Context.longToast(stringRes: Int) {
    Toast.makeText(this, stringRes, Toast.LENGTH_LONG).show()
}

/**
 * 弹出LENGTH_LONG的toast
 * @receiver Context
 * @param string Int
 */
fun Context.longToast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_LONG).show()
}

/**
 * LENGTH_SHORT
 * @receiver Context
 * @param stringRes Int
 */
fun Context.shotToast(stringRes: Int) {
    Toast.makeText(this, stringRes, Toast.LENGTH_SHORT).show()
}

/**
 * LENGTH_SHORT
 * @receiver Context
 * @param string Int
 */
fun Context.shotToast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}