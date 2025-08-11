package com.catchpig.kmvvm.entity

import kotlinx.serialization.Serializable

@Serializable
data class Banner(val desc: String, val imagePath: String, val title: String)
