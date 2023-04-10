package com.example.swifttext.common

sealed class Resource<T>(val data: Any? = false, val message: String? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data = data)
    class Success<T>(data: T, message: String? = null) : Resource<T>(data, message)
    class Error<T>(message: String, data: T? = null) : Resource<T>(message = message)
}
