package com.zhj.bluetooth.sdkdemo.utils.extension

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.zhj.bluetooth.sdkdemo.data.model.constant.Constants.KEY_ARG
import com.zhj.bluetooth.sdkdemo.ui.base.BaseArgs
import com.zhj.bluetooth.sdkdemo.ui.base.BaseListFragment
import com.zhj.bluetooth.sdkdemo.ui.base.BaseListState
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.controller.BaseGridTypedEpoxyController
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.controller.BaseTypedEpoxyController
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.controller.SimpleController
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.controller.SimpleGridController

fun Fragment.createFragment(fragment: Fragment, arg: BaseArgs? = null) =
    fragment.apply {
        if(arg != null)
            arguments = Bundle().apply { putSerializable(KEY_ARG, arg) }
    }


fun <S: BaseListState<*>> BaseListFragment<S, *>.simpleController(
    buildModels: BaseTypedEpoxyController<S>.(state: S) -> Unit
) = SimpleController<S> {
    buildModels(vm.state as S)
}

fun <S: BaseListState<*>> BaseListFragment<S, *>.simpleGridController(
    buildModels: BaseGridTypedEpoxyController<S>.(state: S) -> Unit
) = SimpleGridController<S> {
    buildModels(vm.state as S)
}