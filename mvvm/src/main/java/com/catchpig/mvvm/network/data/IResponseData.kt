package com.catchpig.mvvm.network.data

import kotlinx.serialization.json.JsonElement

interface IResponseData<T : JsonElement> {
    /**
     * 返回数据
     * @return T
     */
    fun data(): T?

    /**
     * 成功返回数据
     * @return String
     */
    fun isSuccess(): String

    /**
     * 返回错误码
     * @return String
     */
    fun getErrorCode(): String

    /**
     * 错误信息
     * @return String
     */
    fun getErrorMessage(): String
}