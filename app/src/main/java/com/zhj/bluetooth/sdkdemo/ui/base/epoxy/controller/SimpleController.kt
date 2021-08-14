package com.zhj.bluetooth.sdkdemo.ui.base.epoxy.controller

import com.zhj.bluetooth.sdkdemo.ui.base.BaseListState


class SimpleController<S: BaseListState<*>>(
    val buildModelsCallback: BaseTypedEpoxyController<S>.() -> Unit = {}
) : BaseTypedEpoxyController<S>() {
    override fun buildContentModels(data: S) {
        buildModelsCallback()
    }
}