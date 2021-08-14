package com.zhj.bluetooth.sdkdemo.utils.extension


fun toast(string: String?) {
    com.zhj.bluetooth.sdkdemo.utils.util.ToastUtils.showShort(string)
}

fun toast(int: Int) {
    com.zhj.bluetooth.sdkdemo.utils.util.ToastUtils.showShort(int)
}

fun longToast(string: String) {
    com.zhj.bluetooth.sdkdemo.utils.util.ToastUtils.showLong(string)
}

fun longToast(int: Int) {
    com.zhj.bluetooth.sdkdemo.utils.util.ToastUtils.showLong(int)
}