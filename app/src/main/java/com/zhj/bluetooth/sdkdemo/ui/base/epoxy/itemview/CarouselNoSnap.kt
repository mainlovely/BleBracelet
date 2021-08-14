package com.zhj.bluetooth.sdkdemo.ui.base.epoxy.itemview

import android.content.Context
import android.util.AttributeSet
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.ModelView.Size

/**
 * 用戶拉動 recyclerview 時, 當不需要自行滑動操作時, 就需要用到 CarouselNoSnap
 */
@ModelView(saveViewState = true, autoLayout = Size.MATCH_WIDTH_WRAP_HEIGHT)
class CarouselNoSnap @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : Carousel(context, attrs, defStyle) {

    override fun getSnapHelperFactory(): Nothing? = null
}