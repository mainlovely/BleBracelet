package com.zhj.bluetooth.sdkdemo.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.qmuiteam.qmui.widget.QMUIRadiusImageView
import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.utils.extension.*
import com.zhj.bluetooth.sdkdemo.utils.util.setHeadImage
import com.zhj.bluetooth.sdkdemo.utils.util.setOnSingleClickListener
import com.zhj.bluetooth.sdkdemo.utils.util.setOnSingleClickListenerWithAnim

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CJMAppBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    // status bar view
    private var statusBarView: View? = null

    // bar view
    private var barView: FrameLayout? = null

    // main bar
    var mainTitle: TextView? = null
    var mainSubTitle: TextView? = null
    private var mainDescription: TextView? = null
    var mainRightIcon: ImageView? = null
    var mainRightIcon2: ImageView? = null
    private var imagePoint: ImageView? = null
    var mainRightTv: TextView? = null

//    // back bar
    var backAppBarLogo: QMUIRadiusImageView? = null
    var backTitle: TextView? = null
    var subTitle: TextView? = null
    private var backLeft: LinearLayout? = null
    private var backLeftIcon: ImageView? = null
    var backRightIcon: ImageView? = null
    private var backLeftTitle: TextView? = null
    private var backRightTv: TextView? = null

    init {
        val view = inflate(context, R.layout.view_app_bar, this)
        statusBarView = view.findViewById(R.id.status_bar_view)
        barView = view.findViewById(R.id.bar_view)
    }

    @CallbackProp
    fun onClickMainRightIcon(listener: OnClickListener?) {
        mainRightIcon?.setOnSingleClickListenerWithAnim(listener)
    }

    @CallbackProp
    fun onClickMainRightIcon2(listener: OnClickListener?) {
        mainRightIcon2?.setOnSingleClickListenerWithAnim(listener)
    }

    @CallbackProp
    fun onClickMainRightTv(listener: OnClickListener?) {
        mainRightTv?.setOnSingleClickListenerWithAnim(listener)
    }

    @CallbackProp
    fun onClickBackLeftIcon(listener: OnClickListener?) {
        backLeft?.setOnSingleClickListener(listener)
    }

    @CallbackProp
    fun onClickBackRightIcon(listener: OnClickListener?) {
        backRightIcon?.setOnSingleClickListenerWithAnim(listener)
    }

    @CallbackProp
    fun onClickBackRightTv(listener: OnClickListener?) {
        backRightTv?.setOnSingleClickListener(listener)
    }

    @ModelProp
    fun setBarInfo(info: MainBarInfo) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mainView = inflater.inflate(R.layout.view_app_bar_main, null, false) as View

        mainTitle = mainView.findViewById(R.id.main_title)
        mainSubTitle = mainView.findViewById(R.id.main_subtitle)
        mainDescription = mainView.findViewById(R.id.main_description)
        mainRightIcon = mainView.findViewById(R.id.main_right_icon)
        mainRightIcon2 = mainView.findViewById(R.id.main_right_icon2)
        imagePoint = mainView.findViewById(R.id.imagePoint)
        mainRightTv = mainView.findViewById(R.id.main_right_tv)

        mainTitle?.text = info.title
        mainSubTitle?.text = info.subTitle
        mainDescription?.text = info.description
        if (info.rightIcon != 0) mainRightIcon?.setImageResource(info.rightIcon)
        if (info.rightIcon2 != 0) mainRightIcon2?.setImageResource(info.rightIcon2)
        mainRightTv?.visiableIf { info.rightTv.isNotBlank() }
        mainRightTv?.text = info.rightTv

        barView?.removeAllViews()
        barView?.addView(mainView)
    }

    @ModelProp
    fun setBarInfo(info: BackBarInfo) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val backView = inflater.inflate(R.layout.view_app_bar_back, null, false) as View

        backLeft = backView.findViewById(R.id.back_left)
        backTitle = backView.findViewById(R.id.back_title)
        backAppBarLogo = backView.findViewById(R.id.back_app_bar_logo)
        backLeftIcon = backView.findViewById(R.id.back_left_icon)
        backRightIcon = backView.findViewById(R.id.back_right_icon)
        backLeftTitle = backView.findViewById(R.id.back_left_title)
        backRightTv = backView.findViewById(R.id.back_right_tv)

        statusBarView?.setBackgroundColor(info.backgroundColor.color())
        backView.setBackgroundColor(info.backgroundColor.color())
        backTitle?.text = info.title
//        backAppBarLogo?.visibility = if (info.title.isBlank()) View.GONE else View.VISIBLE
        backLeftTitle?.visibility = if (info.backLeftTitle.isBlank()) View.GONE else View.VISIBLE
        if (info.rightIcon != 0) backRightIcon?.setImageResource(info.rightIcon)
        if (info.leftIcon != 0) backLeftIcon?.setImageResource(info.leftIcon)

        backRightTv?.apply {
            text = info.backRightTitle
            visibility = if (info.backRightTitle.isBlank()) View.GONE else View.VISIBLE
            setBackgroundResource(info.rightTitleBackground)
        }

        barView?.removeAllViews()
        barView?.addView(backView)
    }

    @ModelProp
    fun setBarInfo(info: SubBarInfo) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val backWithSubView = inflater.inflate(R.layout.view_app_bar_sub_title, null, false) as View

        subTitle = backWithSubView.findViewById(R.id.sub_title)
        backLeft = backWithSubView.findViewById(R.id.back_left)
        backTitle = backWithSubView.findViewById(R.id.back_title)
        backAppBarLogo = backWithSubView.findViewById(R.id.back_app_bar_logo)
        backLeftIcon = backWithSubView.findViewById(R.id.back_left_icon)
        backRightIcon = backWithSubView.findViewById(R.id.back_right_icon)
        backLeftTitle = backWithSubView.findViewById(R.id.back_left_title)
        backRightTv = backWithSubView.findViewById(R.id.back_right_tv)

        statusBarView?.setBackgroundColor(info.backgroundColor.color())
        backWithSubView.setBackgroundColor(info.backgroundColor.color())
        backTitle?.text = info.title
        backAppBarLogo?.visibility = if (info.title.isBlank()) View.GONE else View.VISIBLE
        backLeftTitle?.visibility = if (info.backLeftTitle.isBlank()) View.GONE else View.VISIBLE
        if (info.rightIcon != 0) backRightIcon?.setImageResource(info.rightIcon)
        backRightIcon?.visibility = if (info.rightIcon != 0) View.VISIBLE else View.GONE
        backRightTv?.apply {
            text = info.backRightTitle
            visibility = if (info.backRightTitle.isBlank()) View.GONE else View.VISIBLE
        }

        barView?.removeAllViews()
        barView?.addView(backWithSubView)
    }

    fun setBackRightIconVisible(isVisible: Boolean) {
        backRightIcon?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setBackAppBarLogo(url: String, isCircle: Boolean = false) {
        if (backAppBarLogo != null) {
            if (isCircle)
                backAppBarLogo?.resize(com.zhj.bluetooth.sdkdemo.utils.util.ConvertUtils.dp2px(20f), com.zhj.bluetooth.sdkdemo.utils.util.ConvertUtils.dp2px(20f))
            backAppBarLogo?.isCircle = isCircle
            setHeadImage(backAppBarLogo!!, url)
        }
    }

    fun setBackTitle(title: String, needAppBarLogo: Boolean = true) {
        backTitle?.text = title
        backAppBarLogo?.visibility = when (title.isBlank()) {
            true -> View.GONE
            else -> if (needAppBarLogo) View.VISIBLE else View.GONE
        }
    }

    fun setShowImagePoint(isShow: Boolean = false) {
        imagePoint?.visibility = when (isShow) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    fun isShowBackIcon(isShow: Boolean) {
        if (isShow)
            backLeftIcon?.visiable()
        else
            backLeftIcon?.invisiable()
    }
}

data class MainBarInfo(
    var title: String = "",
    var subTitle: String = "",
    var description: String = "",
    var rightIcon: Int = 0,
    var rightIcon2: Int = 0,
    var rightTv: String = ""
) : AppBarInfo()


data class BackBarInfo(
    var title: String = "",
    var leftIcon: Int = 0,
    var rightIcon: Int = 0,
    var backLeftTitle: String = "",
    var backgroundColor: Int = R.color.color_1D1D1D,
    var backRightTitle: String = "",
    var rightTitleBackground: Int = 0
) : AppBarInfo()

data class SubBarInfo(
    var title: String = "",
    var subTitle: String = "",
    var rightIcon: Int = 0,
    var backLeftTitle: String = "",
    var backgroundColor: Int = R.color.color_1D1D1D,
    var backRightTitle: String = ""
) : AppBarInfo()

abstract class AppBarInfo
