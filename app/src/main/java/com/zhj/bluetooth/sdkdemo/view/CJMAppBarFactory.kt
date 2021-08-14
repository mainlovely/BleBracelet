package com.zhj.bluetooth.sdkdemo.view

import android.view.View
import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.utils.extension.visiable

object CJMAppBarFactory {
    private const val MAIN = 1000
    const val MAIN_HOME = MAIN + 1

    private const val BACK = 2000
    const val BACK_SCAN = BACK + 1
    const val BACK_Ble_STATUS = BACK + 2
    const val BACK_Ble_HEART_RATE = BACK + 3
    const val BACK_Ble_SLEEP = BACK + 4
    const val BACK_BLE_SPORT = BACK + 5
    const val BACK_SETTING = BACK + 6
    const val BACK_SWITCH = BACK + 7



    fun create(
        type: Int,
        appbar: CJMAppBar,
        rightSideListener: View.OnClickListener? = null,
        rightSideListener2: View.OnClickListener? = null,
        rightSideListenerTv: View.OnClickListener? = null,
        isShowUnread: Boolean? = false
    ) {

        when (val info = getAppBarInfo(type, appbar)) {
            is MainBarInfo -> {
                appbar.visiable()
                appbar.setBarInfo(info)
                if (isShowUnread != null) appbar.setShowImagePoint(isShowUnread)
                if (rightSideListener != null) appbar.onClickMainRightIcon(rightSideListener)
                if (rightSideListener2 != null) appbar.onClickMainRightIcon2(rightSideListener2)
                if (rightSideListenerTv != null) appbar.onClickMainRightTv(rightSideListenerTv)
            }
            is BackBarInfo -> {
                appbar.visiable()
                appbar.setBarInfo(info)
                if (isShowUnread != null) appbar.setShowImagePoint(isShowUnread)
                if (rightSideListener != null) appbar.onClickBackRightIcon(rightSideListener)
                if (rightSideListener != null) appbar.onClickBackRightTv(rightSideListener)
            }
            is SubBarInfo -> {
                appbar.visiable()
                appbar.setBarInfo(info)
                if (isShowUnread != null) appbar.setShowImagePoint(isShowUnread)
                if (rightSideListener2 != null) appbar.onClickBackRightIcon(rightSideListener2)
                if (rightSideListener != null) appbar.onClickBackRightTv(rightSideListener)
            }
        }
    }

    private fun getAppBarInfo(type: Int, appbar: CJMAppBar): AppBarInfo = when (type) {

        /** Main Bar **/
        MAIN_HOME -> MainBarInfo(
            title = "BleBracelet",
            subTitle = "",
            description = "用真心对世界微笑",
            rightIcon = R.mipmap.ic_setting,
            rightIcon2 = R.mipmap.icon_scan
        )

        /** Back Bar **/
        BACK_SCAN -> BackBarInfo(title = "搜索设备", backgroundColor = R.color.transparent)
        BACK_Ble_STATUS -> BackBarInfo(title = "设备状态", backgroundColor = R.color.transparent)
        BACK_Ble_HEART_RATE -> BackBarInfo(title = "历史心率", backgroundColor = R.color.transparent)
        BACK_Ble_SLEEP -> BackBarInfo(title = "历史睡眠", backgroundColor = R.color.transparent)
        BACK_BLE_SPORT -> BackBarInfo(title = "历史运动", backgroundColor = R.color.transparent)
        BACK_SETTING -> BackBarInfo(title = "设置档案", backgroundColor = R.color.transparent)
        BACK_SWITCH -> BackBarInfo(title = "开关设置", backgroundColor = R.color.transparent)

        else -> throw Exception("app bar not found")
    }
}