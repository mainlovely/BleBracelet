package com.zhj.bluetooth.sdkdemo.ui.ble.status

import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.databinding.FragmentBleStatusBinding
import com.zhj.bluetooth.sdkdemo.ui.base.BaseFragment
import com.zhj.bluetooth.sdkdemo.utils.annotation.BindFragment
import com.zhj.bluetooth.sdkdemo.utils.extension.string
import com.zhj.bluetooth.sdkdemo.view.CJMAppBar
import com.zhj.bluetooth.sdkdemo.view.CJMAppBarFactory
import com.zhj.bluetooth.zhjbluetoothsdk.bean.BLEDevice
import com.zhj.bluetooth.zhjbluetoothsdk.bean.DeviceState
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleCallback
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult
import org.koin.androidx.viewmodel.ext.android.viewModel

@BindFragment(layout = R.layout.fragment_ble_status)
class BleDeviceStatusFragment: BaseFragment<FragmentBleStatusBinding, BleDeviceStatusVM>(){
    override fun createViewModel() = viewModel<BleDeviceStatusVM>()

    override fun observeViewModel(vm: BleDeviceStatusVM) {
        vm.navigator = object : BleDeviceStatusNavigator{

        }
    }

    override fun setAppBarInfo(appBar: CJMAppBar) {
        CJMAppBarFactory.create(CJMAppBarFactory.BACK_Ble_STATUS, appBar)
    }

    override fun initView() {
        super.initView()
        BleSdkWrapper.getDeviceState(bleCallBack)
    }

    private val bleCallBack = object : BleCallback{
        override fun complete(p0: Int, p1: Any?) {
            val result = p1 as HandlerBleDataResult
            when(result.data){
                is DeviceState -> {
                    val deviceState = (p1.data as DeviceState)
                    vm.tvScreenLight.value = R.string.device_state_screen_brightness.string() + deviceState.screenLight
                    vm.tvScreenTime.value = R.string.device_state_bright_duration.string() + deviceState.screenTime
                    vm.tvScreenTheme.value = R.string.device_state_theme.string() + deviceState.theme
                    //  0x00：英文 0x01：中文 0x02: 俄罗斯语 0x03: 乌克兰语 0x04：法语 0x05：西班牙语
                    //  0x06：葡萄牙语 0x07：德语 0x08：日本 0x09：波兰 0x0A：意大利
                    //0x0B：罗马尼亚 0x0C: 繁体中文 0x0D: 韩语
                    vm.tvLanguage.value = R.string.device_state_language.string() + when(deviceState.language){
                        0 -> "英文"
                        1 -> "中文"
                        2 -> "俄罗斯语"
                        3 -> "乌克兰语"
                        4 -> "法语"
                        5 -> "西班牙语"
                        6 -> "葡萄牙语"
                        7 -> "德语"
                        8 -> "日语"
                        9 -> "波兰"
                        10 -> "意大利"
                        11 -> "罗马尼亚"
                        12 -> "繁体中文"
                        else -> "韩语"
                    }
                    //0x00:公制 0x01:英制
                    vm.tvUnit.value = R.string.device_state_unit.string() + when(deviceState.unit){
                        0 -> "公制"
                        else -> "英制"
                    }
                    //0x00：24 小时制 0x01：12 小时制
                    vm.tvTimeFormat.value = R.string.device_state_time_system.string() + when(deviceState.timeFormat){
                        0 -> "24 小时制"
                        else -> "12 小时制"
                    }
                    //0x00:关闭 0x01:开启
                    vm.tvUpHander.value = R.string.device_state_handle_up.string() + when(deviceState.upHander){
                        0 -> "关闭"
                        else -> "开启"
                    }
                    //0x00:关闭 0x01:开启
                    vm.tvMusic.value = R.string.device_state_music_control.string() + when(deviceState.isMusic){
                        0 -> "关闭"
                        else -> "开启"
                    }
                    //0x00:关闭 0x01:开启
                    vm.tvNotice.value = R.string.device_state_messagr_swith.string() + when(deviceState.isNotice){
                        0 -> "关闭"
                        else -> "开启"
                    }
                    //0x01:左手 0x02:右手 其他：无效
                    vm.tvHandHabits.value = R.string.device_state_hand_hibits.string() + when(deviceState.handHabits){
                        1 -> "左手"
                        2 -> "右手"
                        else -> "无效"
                    }

                }
                is BLEDevice -> {
                    val device = (p1.data as BLEDevice)
                    vm.tvVersion.value = R.string.device_version.string() + device.mDeviceVersion
                }
            }
        }

        override fun setSuccess() {
        }

    }

}