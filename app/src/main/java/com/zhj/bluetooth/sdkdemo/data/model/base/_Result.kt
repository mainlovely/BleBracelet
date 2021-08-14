package com.zhj.bluetooth.sdkdemo.data.model.base

import com.zhj.bluetooth.sdkdemo.data.model.constant.RespCode


sealed class _Result<out R> {
    open val resultCode: String? = ""
    open val msg: String? = ""

    data class Success<out T>(val data: T?,
                              override val msg: String? = "success",
                              override val resultCode: String? = RespCode.SUCCESS) : _Result<T>()
    data class Fail<out T>(val data: T?,
                           override val msg: String? = "fail",
                           override val resultCode: String? = "99999") : _Result<T>()
    data class Error(val t: Throwable) : _Result<Nothing>()

    object Loading : _Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=${t.message}]"
            Loading -> "Loading"
            else -> ""
        }
    }
}
