package com.zhj.bluetooth.sdkdemo.ui.base.epoxy.controller

import com.zhj.bluetooth.sdkdemo.ui.base.BaseListState

class SimpleGridController<S: BaseListState<*>>(
    val buildModelsCallback: BaseGridTypedEpoxyController<S>.() -> Unit = {}
) : BaseGridTypedEpoxyController<S>() {
    override fun buildContentModels(data: S) {
        buildModelsCallback()
    }
}