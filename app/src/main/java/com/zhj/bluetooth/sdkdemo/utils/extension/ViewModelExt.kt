package com.zhj.bluetooth.sdkdemo.utils.extension

import androidx.lifecycle.viewModelScope
import com.zhj.bluetooth.sdkdemo.BuildConfig
import com.zhj.bluetooth.sdkdemo.data.model.base.HideLoadingState
import com.zhj.bluetooth.sdkdemo.data.model.base.ShowLoadingState
import com.zhj.bluetooth.sdkdemo.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun BaseViewModel<*>.launch(needLoading: Boolean = false, block: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launch {
        try {
            if(needLoading) mLoading.value = ShowLoadingState
            block.invoke(this)
        } catch (ex: Exception) {
            if(BuildConfig.DEBUG) {
                loge("Exception : $ex")
                println(ex.printStackTrace())
//                toast("Exception : $ex")
            }
        } finally {
            if(needLoading) mLoading.value = HideLoadingState
        }
    }
}