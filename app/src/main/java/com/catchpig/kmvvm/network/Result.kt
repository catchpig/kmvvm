package com.catchpig.kmvvm.network

import com.catchpig.mvvm.network.data.BaseResponseData
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Serializable
data class Result(val errorCode: Int, val errorMsg: String?, val data: JsonElement?) :
    BaseResponseData<JsonElement> {
    companion object {
        const val SUCCESS_CODE = "0"
    }

    override fun data(): JsonElement? {
        return data
    }

    override fun isSuccess(): String {
        return SUCCESS_CODE
    }

    override fun getErrorCode(): String {
        return errorCode.toString()
    }

    override fun getErrorMessage(): String {
        return errorMsg!!
    }
}