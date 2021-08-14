package com.zhj.bluetooth.sdkdemo.ui.ble.status

import androidx.lifecycle.MutableLiveData
import com.zhj.bluetooth.sdkdemo.ui.base.BaseNavigator
import com.zhj.bluetooth.sdkdemo.ui.base.BaseViewModel

interface BleDeviceStatusNavigator: BaseNavigator{

}

class BleDeviceStatusVM : BaseViewModel<BleDeviceStatusNavigator>(){
    val tvVersion = MutableLiveData<String>()
    val tvScreenLight = MutableLiveData<String>()
    val tvScreenTime = MutableLiveData<String>()
    val tvScreenTheme = MutableLiveData<String>()
    val tvLanguage = MutableLiveData<String>()
    val tvUnit = MutableLiveData<String>()
    val tvTimeFormat = MutableLiveData<String>()
    val tvUpHander = MutableLiveData<String>()
    val tvMusic = MutableLiveData<String>()
    val tvNotice = MutableLiveData<String>()
    val tvHandHabits = MutableLiveData<String>()

}