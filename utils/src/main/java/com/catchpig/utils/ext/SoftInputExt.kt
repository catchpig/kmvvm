package com.catchpig.utils.ext

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * 隐藏软键盘
 * @receiver Activity
 */
fun Activity.hideSoftInput() {
    val inputMethodManager =
        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(window.decorView.windowToken, 0)
}

/**
 * 隐藏软键盘
 * @receiver View
 */
fun View.hideSoftInput() {
    val inputMethodManager =
        context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * 隐藏软键盘
 * @receiver AppCompatEditText
 */
fun EditText.showSoftInput() {
    requestFocus()
    val inputMethodManager =
        context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}