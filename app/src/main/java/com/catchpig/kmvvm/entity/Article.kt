package com.catchpig.kmvvm.entity

import kotlinx.serialization.Serializable

@Serializable
data class Article(val title: String, val shareDate: Long)
