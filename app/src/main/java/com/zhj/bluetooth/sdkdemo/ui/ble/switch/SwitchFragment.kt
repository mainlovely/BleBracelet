package com.zhj.bluetooth.sdkdemo.ui.ble.switch

import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.databinding.FragmentSwitchBinding
import com.zhj.bluetooth.sdkdemo.ui.base.BaseFragment
import com.zhj.bluetooth.sdkdemo.utils.annotation.BindFragment
import com.zhj.bluetooth.sdkdemo.utils.extension.isVisiableOrGone
import com.zhj.bluetooth.sdkdemo.utils.extension.setSafeOnClickListener
import com.zhj.bluetooth.sdkdemo.utils.extension.visiableIf
import com.zhj.bluetooth.sdkdemo.utils.util.ToastUtils
import com.zhj.bluetooth.sdkdemo.view.CJMAppBar
import com.zhj.bluetooth.sdkdemo.view.CJMAppBarFactory
import com.zhj.bluetooth.zhjbluetoothsdk.bean.HeartRateInterval
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleCallback
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult
import com.zhj.bluetooth.zhjbluetoothsdk.util.LogUtil
import org.koin.androidx.viewmodel.ext.android.viewModel

@BindFragment(layout = R.layout.fragment_switch)
class SwitchFragment: BaseFragment<FragmentSwitchBinding, SwitchVM>() {
    override fun createViewModel() = viewModel<SwitchVM>()

    override fun observeViewModel(vm: SwitchVM) {
        vm.navigator = object : SwitchNavigator{

        }

    }

    override fun setAppBarInfo(appBar: CJMAppBar) {
        CJMAppBarFactory.create(CJMAppBarFactory.BACK_SWITCH, appBar)
    }

    override fun initData() {
        super.initData()
        getHeartOpen()
        getHeartWarm()
    }

    override fun initListener() {
        super.initListener()
        binding.switchHeart.setOnCheckedChangeListener { p0, p1 ->
            setHeartOpen(p1)
        }

        binding.switchHeartWarm.setOnCheckedChangeListener { p0, p1 ->
            binding.rlSetHeartWarm.isVisiableOrGone(p1)
            setHeartWarm(false)
        }

        binding.tvConfirm.setSafeOnClickListener {
            if( binding.etHeartWarm.text.toString().toInt() <=0){
                ToastUtils.showShort("最大心率必须大于0")
                return@setSafeOnClickListener
            }
            setHeartWarm(true)
        }
    }

    private fun getHeartOpen(){
        //获取心率检测
        BleSdkWrapper.getHeartOpen(object : BleCallback {
            override fun complete(resultCode: Int, data: Any?) {
                if(data is HandlerBleDataResult){
                    val result: HandlerBleDataResult? = data as HandlerBleDataResult
                    if(result?.data is Boolean){
                        val isOpen = result.data as Boolean
                        LogUtil.d(isOpen.toString() + "")
                        binding.switchHeart.isChecked = isOpen
                    }
                }
            }

            override fun setSuccess() {}
        })
    }

    private fun setHeartOpen(isOpen: Boolean){
        BleSdkWrapper.setHeartTest(isOpen, object : BleCallback {
            override fun complete(i: Int, o: Any?) {
            }
            override fun setSuccess() {}
        })
    }


    /**
     * 获取心率报警开关
     */
    private fun getHeartWarm(){
        BleSdkWrapper.getHartRong(object : BleCallback {
            override fun complete(i: Int, data: Any?) {
                if(data is HandlerBleDataResult){
                    val result = data as HandlerBleDataResult
                    if(result.data is HeartRateInterval){
                        val interval = result.data as HeartRateInterval
                        binding.switchHeartWarm.isChecked = interval.isCustomHr
                        binding.etHeartWarm.setText(interval.maxHr.toString())
                    }

                }

            }

            override fun setSuccess() {}
        })
    }

    private fun setHeartWarm(isShowToast: Boolean){
        BleSdkWrapper.setHeartRange(
            when(binding.switchHeartWarm.isChecked){
                true -> 1
                else -> 0
            },
            binding.etHeartWarm.text.toString().toInt(),
            object : BleCallback {
                override fun complete(i: Int, o: Any?) {
                    if(isShowToast){
                        ToastUtils.showShort("设置成功")
                    }
                }

                override fun setSuccess() {}
            })
    }

}