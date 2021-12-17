package com.catchpig.annotation.interfaces

import kotlinx.serialization.json.Json
import retrofit2.Converter

interface SerializationConverter<F, T> : Converter<F, T> {
    var json: Json
}