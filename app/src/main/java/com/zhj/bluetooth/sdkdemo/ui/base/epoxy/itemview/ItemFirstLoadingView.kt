package com.zhj.bluetooth.sdkdemo.ui.base.epoxy.itemview

import androidx.databinding.ViewDataBinding
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.databinding.ItemFirstLoadingViewBinding
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.BaseDataBindingEpoxyModel
import com.zhj.bluetooth.sdkdemo.utils.extension.color

@EpoxyModelClass(layout = R.layout.item_first_loading_view)
abstract class ItemFirstLoadingView: BaseDataBindingEpoxyModel() {
    @EpoxyAttribute var loadingColor: Int? = 0

    override fun onBindData(binding: ViewDataBinding, previouslyBoundModel: EpoxyModel<*>?) {
        super.onBindData(binding, previouslyBoundModel)
        (binding as ItemFirstLoadingViewBinding).apply {
            loading.setColor(loadingColor?.color() ?: R.color.colorPrimaryRed.color())
        }
    }
}
