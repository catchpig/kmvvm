package com.catchpig.kotlin_mvvm.mvp.recycle

import com.catchpig.annotation.Prefs
import com.catchpig.annotation.PrefsField

/**
 *
 * @author TLi2
 **/
@Prefs
data class User (
        @PrefsField
        val name:String,
        @PrefsField
        val isMan:Boolean = false,
        @PrefsField
        val number:Float = 0.1f,
        @PrefsField
        val age:Int = 27,
        @PrefsField
        val money:Double = 12.0,
        val nu:MutableList<String> = mutableListOf()
)