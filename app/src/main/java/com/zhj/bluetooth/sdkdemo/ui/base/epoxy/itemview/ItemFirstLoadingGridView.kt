package com.zhj.bluetooth.sdkdemo.ui.base.epoxy.itemview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.databinding.ItemFirstLoadingGridViewBinding
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.BaseDataBindingEpoxyModel
import com.zhj.bluetooth.sdkdemo.utils.extension.color

@EpoxyModelClass(layout = R.layout.item_first_loading_grid_view)
abstract class ItemFirstLoadingGridView: BaseDataBindingEpoxyModel() {
    @EpoxyAttribute var loadingColor: Int? = 0

    override fun onBindData(binding: ViewDataBinding, previouslyBoundModel: EpoxyModel<*>?) {
        super.onBindData(binding, previouslyBoundModel)
        val layoutParams = binding.root.layoutParams

        /**
         * 讓瀑布流的emptyview重回中間位置
         */
        if (layoutParams != null && layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = true
        }

        (binding as ItemFirstLoadingGridViewBinding).apply {
            loading.setColor(loadingColor?.color() ?: R.color.colorPrimaryRed.color())
        }
    }
}
