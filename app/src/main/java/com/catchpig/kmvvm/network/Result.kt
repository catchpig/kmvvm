package com.catchpig.kmvvm.network

import com.catchpig.mvvm.network.data.BaseResponseData

data class Result<T>(val errorCode: Int, val errorMsg: String, val data: T) :
    BaseResponseData<T> {
    companion object {
        const val SUCCESS_CODE = "0"
    }

    override fun data(): T {
        return data
    }

    override fun isSuccess(): String {
        return SUCCESS_CODE
    }

    override fun getErrorCode(): String {
        return errorCode.toString()
    }

    override fun getErrorMessage(): String {
        return errorMsg
    }
}