package com.zhj.bluetooth.sdkdemo.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.zhj.bluetooth.sdkdemo.MainViewModel
import com.zhj.bluetooth.sdkdemo.ui.base.dialog.DialogVM
import com.zhj.bluetooth.sdkdemo.ui.ble.heartrate.HeartRateListVM
import com.zhj.bluetooth.sdkdemo.ui.ble.scanning.ScanningBleListVM
import com.zhj.bluetooth.sdkdemo.ui.ble.setting.SettingVM
import com.zhj.bluetooth.sdkdemo.ui.ble.sleep.SleepListVM
import com.zhj.bluetooth.sdkdemo.ui.ble.sport.SportHistoryListVM
import com.zhj.bluetooth.sdkdemo.ui.ble.status.BleDeviceStatusVM
import com.zhj.bluetooth.sdkdemo.ui.ble.switch.SwitchVM


val vmModule = module {
    viewModel { DialogVM() }
    viewModel { MainViewModel(get()) }
    viewModel { ScanningBleListVM() }
    viewModel { BleDeviceStatusVM() }
    viewModel { HeartRateListVM() }
    viewModel { SleepListVM() }
    viewModel { SportHistoryListVM() }
    viewModel { SettingVM(get()) }
    viewModel { SwitchVM() }
}