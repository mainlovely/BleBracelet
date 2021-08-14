package com.zhj.bluetooth.sdkdemo.ui.base.epoxy.itemview

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.BaseDataBindingEpoxyModel

@EpoxyModelClass(layout = R.layout.item_no_network_view)
abstract class ItemNoNetworkView: BaseDataBindingEpoxyModel() {
    @EpoxyAttribute var emptyView: Int? = 0
    @EpoxyAttribute var emptyText: String? = ""
    @EpoxyAttribute var emptyButtonText: String? = ""
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) var onClickListener: View.OnClickListener? = null

    override fun onBindData(binding: ViewDataBinding, previouslyBoundModel: EpoxyModel<*>?) {
        super.onBindData(binding, previouslyBoundModel)
        val layoutParams = binding.root.layoutParams

        /**
         * 讓瀑布流的emptyview重回中間位置
         */
        if (layoutParams != null && layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = true
        }
    }
}
