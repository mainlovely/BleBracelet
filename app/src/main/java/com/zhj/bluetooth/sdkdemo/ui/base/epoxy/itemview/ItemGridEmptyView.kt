package com.zhj.bluetooth.sdkdemo.ui.base.epoxy.itemview

import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.databinding.ItemEmptyViewBinding
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.BaseDataBindingEpoxyModel
import com.zhj.bluetooth.sdkdemo.utils.extension.gone
import com.zhj.bluetooth.sdkdemo.utils.extension.visiable

@EpoxyModelClass(layout = R.layout.item_empty_view)
abstract class ItemGridEmptyView : BaseDataBindingEpoxyModel() {
    @EpoxyAttribute
    var data: ItemEmptyViewData? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var onClickListener: View.OnClickListener? = null

    override fun onBindData(binding: ViewDataBinding, previouslyBoundModel: EpoxyModel<*>?) {
        super.onBindData(binding, previouslyBoundModel)
        val layoutParams = binding.root.layoutParams

        /**
         * 讓瀑布流的emptyview重回中間位置
         */
        if (layoutParams != null && layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = true
        } else {
            binding.root.layoutParams = StaggeredGridLayoutManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            (binding.root.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
        }

        (binding as ItemEmptyViewBinding).apply {
            textView2.setTextColor(com.zhj.bluetooth.sdkdemo.utils.util.ColorUtils.getColor(data?.textColor ?: R.color.white))
            if (data?.emptyButtonText.isNullOrBlank()) {
                imageButton.gone()
            } else {
                imageButton.visiable()
            }
        }
    }
}
