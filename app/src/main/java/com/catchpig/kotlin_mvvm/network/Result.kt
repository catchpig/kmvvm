package com.catchpig.kotlin_mvvm.network

data class Result<T>(val errorCode: Int, val errorMsg: String, val data: T) {
    companion object {
        const val SUCCESS_CODE = 0
    }
}