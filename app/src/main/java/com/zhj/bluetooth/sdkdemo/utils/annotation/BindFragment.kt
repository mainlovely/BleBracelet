package com.zhj.bluetooth.sdkdemo.utils.annotation

import com.zhj.bluetooth.sdkdemo.ui.base.BaseFragment.Companion.NO_BINDING_VARIABLE


annotation class BindFragment(
    val layout: Int = 0,
    val bindingVariable: Int = NO_BINDING_VARIABLE,
    val hasRefresh: Boolean = true,
    val hasLoadMore: Boolean = true,
    val hasEventBus: Boolean = false,
    val hasCoord: Boolean = false,
    val hasTransparentToolBar: Boolean = false,
    val hasLazyLoading: Boolean = false
)