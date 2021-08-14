package com.zhj.bluetooth.sdkdemo.ui.ble.setting

import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.data.source.local.Prefs
import com.zhj.bluetooth.sdkdemo.databinding.FragmentSettingBinding
import com.zhj.bluetooth.sdkdemo.ui.base.BaseFragment
import com.zhj.bluetooth.sdkdemo.utils.annotation.BindFragment
import com.zhj.bluetooth.sdkdemo.utils.extension.selfDestroy
import com.zhj.bluetooth.sdkdemo.utils.util.ToastUtils
import com.zhj.bluetooth.sdkdemo.view.CJMAppBar
import com.zhj.bluetooth.sdkdemo.view.CJMAppBarFactory
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by seven on 2020/7/11
 */
@BindFragment(layout = R.layout.fragment_setting)
class SettingFragment: BaseFragment<FragmentSettingBinding, SettingVM>() {
    override fun createViewModel() = viewModel<SettingVM>()

    override fun observeViewModel(vm: SettingVM) {
        vm.navigator = object: SettingNavigator{
            override fun submitSuccess() {
                ToastUtils.showShort("提交成功")
                selfDestroy()
            }
        }
    }

    override fun setAppBarInfo(appBar: CJMAppBar) {
        CJMAppBarFactory.create(CJMAppBarFactory.BACK_SETTING, appBar)
    }

    override fun initData() {
        super.initData()
        vm.bleDevice = Prefs.bindBleDevice?.bleDevice
    }
}