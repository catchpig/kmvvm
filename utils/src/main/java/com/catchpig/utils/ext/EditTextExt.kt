package com.catchpig.utils.ext

import android.text.Editable
import android.widget.EditText

/**
 * 给输入框赋值
 */
fun EditText.setTextValue(text:String?){
    this.text = Editable.Factory.getInstance().newEditable(text)
}