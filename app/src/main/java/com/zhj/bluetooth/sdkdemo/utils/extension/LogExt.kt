package com.zhj.bluetooth.sdkdemo.utils.extension

import com.orhanobut.logger.Logger

fun log(s: String) {
    Logger.d(s)
}

fun loge(s: String) {
    Logger.e(s)
}

fun logJson(s: String) {
    Logger.json(s)
}