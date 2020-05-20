package com.example.thrive.api

import com.example.thrive.api.Status.SUCCESS
import com.example.thrive.api.Status.LOADING
import com.example.thrive.api.Status.ERROR

/**
 * Generic wrapper to encapsulate response objects to be used in view classes.
 */
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        fun <T> error(msg: String): Resource<T> {
            return Resource(ERROR, null, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(LOADING, data, null)
        }
    }
}