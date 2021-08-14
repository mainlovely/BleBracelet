package com.zhj.bluetooth.sdkdemo.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhj.bluetooth.sdkdemo.data.model.base.State

abstract class BaseViewModel<N> : ViewModel() {
    open var TAG = "BaseViewModel"
    val mLoading by lazy { MutableLiveData<State>() }
    val loading: LiveData<State>
        get() = mLoading

    /**
     * 用作 vm 跟 fragment 溝通橋樑
     **/
    var navigator: N? = null

    /**
     * 只適用於 fragment 跟 vm inheritance 之用
     * 用法可以參考
     *
     **/
    var childNavigator: BaseNavigator? = null

    override fun onCleared() {
        super.onCleared()
        navigator = null
        childNavigator = null
    }
}