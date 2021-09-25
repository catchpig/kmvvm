package com.catchpig.utils.ext

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import androidx.core.widget.addTextChangedListener

/**
 * 保留小数点位数
 * @param number 小数点位数
 */
fun EditText.keepDecimalListener(number: Int, callback: (text: String) -> Unit) {
    addTextChangedListener {
        val text = it.toString()
        if (text.isNotEmpty()) {
            if (text.substring(0, 1) == ".") {
                val text = "0$text"
                setText(text)
                setSelection(text.length)
                return@addTextChangedListener
            }
            if (text.contains(".")) {
                var list = text.split(".")
                if (list[1].length > number) {
                    val text = "${list[0]}.${list[1].subSequence(0, number)}"
                    setText(text)
                    setSelection(text.length)
                    return@addTextChangedListener
                }
            }
            callback(text)
        }
    }
}

/**
 * 保留小数点位数
 * @param number 小数点位数
 */
fun EditText.keepDecimal(number: Int) {
    addTextChangedListener {
        val text = it.toString()
        if (text.isNotEmpty()) {
            if (text.substring(0, 1) == ".") {
                val text = "0$text"
                setText(text)
                setSelection(text.length)
                return@addTextChangedListener
            }
            if (text.contains(".")) {
                var list = text.split(".")
                if (list[1].length > number) {
                    val text = "${list[0]}.${list[1].subSequence(0, number)}"
                    setText(text)
                    setSelection(text.length)
                    return@addTextChangedListener
                }
            }
        }
    }
}

/**
 * 隐藏密码
 */
fun EditText.hidePassword() {
    transformationMethod = PasswordTransformationMethod.getInstance()
    setSelection(text.toString().length)
}

/**
 * 显示密码
 */
fun EditText.showPassword() {
    transformationMethod = HideReturnsTransformationMethod.getInstance()
    setSelection(text.toString().length)

}