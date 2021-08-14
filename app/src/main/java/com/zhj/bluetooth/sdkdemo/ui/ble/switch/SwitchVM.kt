package com.zhj.bluetooth.sdkdemo.ui.ble.switch

import androidx.lifecycle.MutableLiveData
import com.zhj.bluetooth.sdkdemo.ui.base.BaseNavigator
import com.zhj.bluetooth.sdkdemo.ui.base.BaseViewModel

interface SwitchNavigator : BaseNavigator {
}

class SwitchVM : BaseViewModel<SwitchNavigator>() {
    val etHeartWarm = MutableLiveData<String>()

}