package com.catchpig.kmvvm.network

import com.catchpig.mvvm.network.data.IResponseData
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Result(val errorCode: Int, val errorMsg: String?, val data: JsonElement?) :
    IResponseData<JsonElement> {
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