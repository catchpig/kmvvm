package com.catchpig.kmvvm.exception

class HttpServerException(val code: Int, message: String) : Exception(message) {
}