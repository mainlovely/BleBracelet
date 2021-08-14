package com.zhj.bluetooth.sdkdemo.ui.base.epoxy

import androidx.databinding.ViewDataBinding
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyModel
import com.zhj.bluetooth.sdkdemo.utils.extension.get

abstract class BaseDataBindingEpoxyModel: DataBindingEpoxyModel() {

    // model on change.
    override fun bind(holder: DataBindingHolder, previouslyBoundModel: EpoxyModel<*>) {
        super.bind(holder, previouslyBoundModel)
        onBindData(holder.get(), previouslyBoundModel)
    }

    // model on create
    override fun bind(holder: DataBindingHolder) {
        super.bind(holder)
        onBindData(holder.get())
    }

    // re-bind the data no matter on create or on change, use it when complex view's implementation is needed.
    open fun onBindData(binding: ViewDataBinding, previouslyBoundModel: EpoxyModel<*>? = null) {

    }
}