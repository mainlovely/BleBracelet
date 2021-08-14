package com.zhj.bluetooth.sdkdemo.ui.base.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.zhj.bluetooth.sdkdemo.MainActivity
import com.zhj.bluetooth.sdkdemo.MainFragment
import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.data.model.constant.Constants
import com.zhj.bluetooth.sdkdemo.ui.base.BaseArgs
import com.zhj.bluetooth.sdkdemo.ui.base.BaseFragment

/**
 * 不做extension Func是因为Activity中有可能用到
 */
object FragmentStack {
    const val STANDARD = 0x11
    const val SINGLE_TOP = 0x12
    const val SINGLE_TASK = 0x13
    const val CLEAR_SINGLE_TOP = 0x14

    var mainActivity: MainActivity? = null
    var stack = arrayListOf<Fragment>()

    private fun addFragmentToStack(fragment: Fragment, stackMode: Int) {
        when (stackMode) {
            CLEAR_SINGLE_TOP -> {
                stack.clear()
                stack.add(fragment)
            }
            SINGLE_TOP -> {
                if (stack.last().javaClass.name == fragment.javaClass.name) {
                    stack.remove(stack.last())
                    stack.add(fragment)
                }
            }
            SINGLE_TASK -> {
                var fmtPos = 0
                stack.forEachIndexed { index, fmt ->
                    if (fmt.javaClass.name == fragment.javaClass.name) {
                        fmtPos = index
                    }
                }

                for (i in stack.indices.reversed()) {
                    if(i >= fmtPos) {
                        removeFmtForStartMode(stack[i])
                        stack.dropLast(stack.size - 1)
                    }
                }

                stack.add(fragment)
            }
            else -> {
                stack.add(fragment)
            }
        }
    }

    /**
     * 所有返回等操作走的是activity的方法
     */
    @Synchronized fun openFragment(from: Fragment?, to: Fragment, arg: BaseArgs? = null, stackMode: Int = STANDARD, needAnimation: Boolean = true, enter: Int = 0, exit: Int = 0) {

        when(stackMode) {
            CLEAR_SINGLE_TOP -> {
                val fm = mainActivity?.supportFragmentManager
                if(fm != null) {
                    for (fragment in fm.fragments) {
                        fm.beginTransaction().remove(fragment).commit()
                    }
                }
            }
        }

        val transaction = mainActivity?.supportFragmentManager?.beginTransaction()

        if(transaction != null) {

            if(arg != null)
                to.arguments = Bundle().apply { putSerializable(Constants.KEY_ARG, arg) }

            if(needAnimation) {
                if(enter != 0 && exit != 0) {
                    transaction.setCustomAnimations(enter, exit)
                } else {
                    transaction.setCustomAnimations(R.anim.v_fragment_enter, R.anim.v_fragment_exit)
                }
            }

            youCannotSeeMe()

            transaction
                .add(R.id.frameContainer, to, to.javaClass.name)
                .commitAllowingStateLoss()

            addFragmentToStack(to, stackMode)

            when(to) {
                is BaseFragment<*, *> -> to.youCanSeeMe()
            }
        }

    }

    /**
     * 可以关闭指定的 也可以关闭当前的
     */
    @Synchronized fun closeFragment(fragment: Fragment?, needAnimation: Boolean = true) {
        if(fragment != null) {
            val transaction = mainActivity?.supportFragmentManager?.beginTransaction()

            if(transaction != null) {
                if(needAnimation) {
                    transaction.setCustomAnimations(R.anim.v_fragment_enter, R.anim.v_fragment_exit)
                }

                transaction.remove(fragment).commit()

                if (stack.contains(fragment))
                    stack.remove(fragment)
            }
        }

        youCanSeeMe()
    }

    @Synchronized private fun removeFmtForStartMode(fragment: Fragment) {
        mainActivity
            ?.supportFragmentManager
            ?.beginTransaction()
            ?.remove(fragment)
            ?.commit()
    }

    /**
     * 清除眼前所有fragment, 直接返回到 MainFragment (即 HOME 主頁面)
     */
    fun clearAllFragmentsBackToHomePage() {

        val fm = mainActivity?.supportFragmentManager

        if(fm != null) {
            for (fragment in fm.fragments) {
                if(fragment !is MainFragment && fragment is BaseFragment<*, *> ) {
                    fm.beginTransaction()
                        .remove(fragment)
                        .commit()

                    if (stack.contains(fragment)) stack.remove(fragment)
                }
            }
        }

        youCanSeeMe()
    }

    fun getTopFragment() : Fragment? {
        return when(stack.size > 0) {
            true -> stack.last()
            else -> null
        }
    }

    fun getFragmentsSize() : Int {
        return stack.size
    }

    /**
     * 如用戶離開APP再重新進入頁面, 用於知道用戶在那個頁面開啟
     */
    fun youCanSeeMe() {
        if(stack.size > 0) {
            when(val lastFragment = stack.last()) {
                is BaseFragment<*, *> -> {
                    lastFragment.youCanSeeMe()
                }
            }
        }
    }

    private fun youCannotSeeMe() {
        if(stack.size > 0) {
            when(val lastFragment = stack.last()) {
                is BaseFragment<*, *> -> {
                    lastFragment.youCannotSeeMe()
                }
            }
        }
    }
}