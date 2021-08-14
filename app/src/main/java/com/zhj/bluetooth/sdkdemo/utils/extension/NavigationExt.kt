package com.zhj.bluetooth.sdkdemo.utils.extension

import androidx.fragment.app.Fragment
import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.ui.base.BaseArgs
import com.zhj.bluetooth.sdkdemo.ui.base.dialog.AlertConfirmDialogFragment
import com.zhj.bluetooth.sdkdemo.ui.base.dialog.ConfirmDialogFragment
import com.zhj.bluetooth.sdkdemo.ui.base.navigation.FragmentStack

/**
 * Destroy 最頂層的fragment
 */
fun Fragment.selfDestroy() {
    FragmentStack.closeFragment(this@selfDestroy)
}

/**
 * Destroy 最頂層的fragment, 不需要动画
 */
fun Fragment.selfDestroyNoAnimation() {
    FragmentStack.closeFragment(this@selfDestroyNoAnimation,false)
}
/**
 * 將新的fragment顯示到最頂部, destroy 原本呼叫的fragment
 */
fun Fragment.replaceTo(toFragment: Fragment, arg: BaseArgs? = null, stackMode: Int = FragmentStack.STANDARD, needAnimation: Boolean = false) {
    FragmentStack.openFragment(null, toFragment, arg, stackMode, needAnimation)
    selfDestroy()
}

/**
 * 將新的fragment顯示到最頂部
 */
fun navigateTo(
    toFragment: Fragment,
    arg: BaseArgs? = null,
    stackMode: Int = FragmentStack.STANDARD,
    needAnimation: Boolean = true,
    enter: Int = 0,
    exit: Int = 0
) {
    FragmentStack.openFragment(null, toFragment, arg, stackMode, needAnimation, enter, exit)
}



/**
 * Confirm Dialog 出現在中間
 */
fun popConfirmDialog(
    title: String,
    listener: ConfirmDialogFragment.ConfirmDialogListener,
    clickOut: Boolean = true,
    tvYesTitle: String? = null,
    tvNoTitle: String? = null
): ConfirmDialogFragment {
    val frag = ConfirmDialogFragment(title, listener, clickOut, tvYesTitle, tvNoTitle)
    FragmentStack.openFragment(
        null,
        frag,
        null,
        FragmentStack.STANDARD,
        true,
        R.anim.fade_in, R.anim.fade_out
    )
    return frag
}

/**
 * 确认弹框
 */
fun popAlertConfirmDialog(
    title: String,
    listener: AlertConfirmDialogFragment.ConfirmDialogListener,
    clickOut: Boolean = true
): AlertConfirmDialogFragment{
    val frag = AlertConfirmDialogFragment(title, listener, clickOut)
    FragmentStack.openFragment(
        null,
        frag,
        null,
        FragmentStack.STANDARD,
        true,
        R.anim.fade_in, R.anim.fade_out
    )
    return frag
}



