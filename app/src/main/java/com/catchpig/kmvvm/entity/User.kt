package com.catchpig.kmvvm.entity

import com.catchpig.annotation.Prefs
import com.catchpig.annotation.PrefsField
import kotlinx.serialization.Serializable

/**
 *
 * @author TLi2
 **/
@Prefs
@Serializable
data class User(
    @PrefsField
    val username: String,
    @PrefsField
    val isMan: Boolean = false,

    @PrefsField
    val number: Float = 0.1f,

    @PrefsField
    val age: Int = 27,

    @PrefsField
    val money: Double = 12.0,
    @PrefsField
    val time: Long = 11,
    val nu: MutableList<String> = mutableListOf()
)