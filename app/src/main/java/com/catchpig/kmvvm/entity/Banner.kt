package com.catchpig.kmvvm.entity

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Banner(val desc: String, val imagePath: String, val title: String)
