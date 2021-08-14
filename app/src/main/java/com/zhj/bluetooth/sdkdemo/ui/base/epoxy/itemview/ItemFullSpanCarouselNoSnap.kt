package com.zhj.bluetooth.sdkdemo.ui.base.epoxy.itemview

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_carousel_no_snap)
abstract class ItemFullSpanCarouselNoSnap: EpoxyModelWithHolder<ItemFullSpanCarouselNoSnap.Holder>() {
    /** 這個如果用了 DoNotHash, 按點讚會沒有反應, 所以不能用 DoNotHash, 不要改 **/
    @EpoxyAttribute lateinit var buildList: (Carousel) -> Unit

    override fun bind(holder: Holder) {
        super.bind(holder)
        val layoutParams = holder.carousel.layoutParams
        if (layoutParams != null && layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = true
        }
        holder.apply {
            carousel.setHasFixedSize(true)
            buildList(carousel)
        }

    }

    class Holder : KotlinEpoxyHolder() {
        val carousel by bind<CarouselNoSnap>(R.id.carousel)
    }
}
