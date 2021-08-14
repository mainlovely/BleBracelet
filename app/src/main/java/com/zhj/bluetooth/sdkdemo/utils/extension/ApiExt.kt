package com.zhj.bluetooth.sdkdemo.utils.extension

import android.util.Log
import com.zhj.bluetooth.sdkdemo.BuildConfig
import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.data.model.base.BaseResponse
import com.zhj.bluetooth.sdkdemo.data.model.base._Result
import com.zhj.bluetooth.sdkdemo.data.model.constant.RespCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.zhj.bluetooth.sdkdemo.utils.util.NetworkUtils

suspend fun <T> apiCall(apiCall: suspend () -> BaseResponse<T>): _Result<T> {
    return withContext(Dispatchers.IO) {
        try {
            val response = apiCall.invoke()
            when(response.code) {
                RespCode.SUCCESS -> _Result.Success(
                    data = response.data,
                    resultCode = response.code,
                    msg = response.message
                )
                else -> {
                    if(BuildConfig.DEBUG)
                        Log.e("apiCall", "api fail -> msg: ${response.message} data: ${response.data}")

                    _Result.Fail(
                        data = response.data,
                        resultCode = response.code,
                        msg = response.message
                    )
                }
            }
        } catch (throwable: Throwable) {
            if(BuildConfig.DEBUG) {
                Log.e("apiCall", "apiCall error -> $throwable")
                toast("$throwable")
            } else {
                if(!NetworkUtils.isConnected())
                    toast(R.string.alert_network)
            }

            _Result.Error(throwable)
        }
    }
}

