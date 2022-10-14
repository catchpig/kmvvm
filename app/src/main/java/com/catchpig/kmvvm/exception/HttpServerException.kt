package com.catchpig.kmvvm.exception

class HttpServerException(val code: String, message: String) : Exception(message) {
}