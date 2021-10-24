package com.catchpig.kotlin_mvvm.exception

class HttpServerException(val code: Int, message: String) : Exception(message) {
}