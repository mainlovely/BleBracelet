package com.zhj.bluetooth.sdkdemo.ui.base.epoxy.itemview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.ModelView.Size

/**
 * 類似於 viewpager 的做法
 */
@ModelView(saveViewState = true, autoLayout = Size.MATCH_WIDTH_WRAP_HEIGHT)
class CarouselPageSnap @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : Carousel(context, attrs, defStyle) {

    override fun getSnapHelperFactory() = object : Carousel.SnapHelperFactory() {
        override fun buildSnapHelper(context: Context?): SnapHelper {
            return PagerSnapHelper()
        }
    }
}