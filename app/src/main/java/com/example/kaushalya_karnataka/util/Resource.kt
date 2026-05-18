package com.example.kaushalya_karnataka.util

/**
 * A generic class that holds a value with its loading status.
 * This is a standard pattern for "Excellent" architecture in Android.
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}
