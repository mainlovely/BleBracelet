package com.zhj.bluetooth.sdkdemo

import android.util.Log
import android.view.View
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.zhj.bluetooth.sdkdemo.data.model.BleDeviceData
import com.zhj.bluetooth.sdkdemo.data.model.ConnectBleSuccessEvent
import com.zhj.bluetooth.sdkdemo.data.source.local.Prefs
import com.zhj.bluetooth.sdkdemo.databinding.FragmentMainBinding
import com.zhj.bluetooth.sdkdemo.ui.base.BaseFragment
import com.zhj.bluetooth.sdkdemo.ui.base.dialog.ConfirmDialogFragment
import com.zhj.bluetooth.sdkdemo.ui.ble.heartrate.HeartRateListFragment
import com.zhj.bluetooth.sdkdemo.ui.ble.scanning.ScanningBleListFragment
import com.zhj.bluetooth.sdkdemo.ui.ble.setting.SettingFragment
import com.zhj.bluetooth.sdkdemo.ui.ble.sleep.SleepListFragment
import com.zhj.bluetooth.sdkdemo.ui.ble.sport.SportHistoryLIstFragment
import com.zhj.bluetooth.sdkdemo.ui.ble.status.BleDeviceStatusFragment
import com.zhj.bluetooth.sdkdemo.ui.ble.switch.SwitchFragment
import com.zhj.bluetooth.sdkdemo.utils.annotation.BindFragment
import com.zhj.bluetooth.sdkdemo.utils.extension.*
import com.zhj.bluetooth.sdkdemo.utils.util.ToastUtils
import com.zhj.bluetooth.sdkdemo.view.CJMAppBar
import com.zhj.bluetooth.sdkdemo.view.CJMAppBarFactory
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleCallbackWrapper
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleManager
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper
import com.zhj.bluetooth.zhjbluetoothsdk.util.Constants
import com.zhj.bluetooth.zhjbluetoothsdk.util.LogUtil
import com.zhj.bluetooth.zhjbluetoothsdk.util.SPHelper
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

@BindFragment(layout = R.layout.fragment_main, hasEventBus = true)
class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>(), OnRefreshListener {
    override fun createViewModel() = viewModel<MainViewModel>()
    override fun observeViewModel(vm: MainViewModel) {
        vm.navigator = object : MainNavigator {
            override fun toDeviceStatus() {
                navigateTo(BleDeviceStatusFragment())
            }

            override fun showDevicePower(power: Int) {
                binding.devicePowerProgress.value = power.toFloat()
            }

            override fun showDeviceGoalStep(curStep: Int, maxStep: Int) {
                binding.progressBarView.maxValue = maxStep.toFloat()
                binding.progressBarView.setValue(curStep.toFloat())
            }

            override fun finishRefresh() {
                refreshFinish()
            }

            override fun toHeartRatePage() {
                navigateTo(HeartRateListFragment())
            }

            override fun toSleepPage() {
                navigateTo(SleepListFragment())
            }

            override fun toSportPage() {
                navigateTo(SportHistoryLIstFragment())
            }

            override fun toAlarmClock() {
                ToastUtils.showShort("请尽请期待")
            }

            override fun toSedentaryReminder() {
                ToastUtils.showShort("请尽请期待")
            }

            override fun toSwitchPage() {
                navigateTo(SwitchFragment())
            }


        }
    }

    override fun setAppBarInfo(appBar: CJMAppBar) {
        CJMAppBarFactory.create(CJMAppBarFactory.MAIN_HOME, appBar,
            rightSideListener = View.OnClickListener {
                if(!BleSdkWrapper.isBind()){
                    ToastUtils.showShort("请先绑定手环设备")
                    return@OnClickListener
                }
                navigateTo(SettingFragment())
        }, rightSideListener2 = View.OnClickListener {
                navigateTo(ScanningBleListFragment())
            },
            rightSideListenerTv = View.OnClickListener {
            if (BleSdkWrapper.isBind()) {
                popConfirmDialog(
                    R.string.disconnect_ble_tip.string(),
                    listener = object : ConfirmDialogFragment.ConfirmDialogListener {
                        override fun onClickYes() {
                            BleSdkWrapper.unBind()
                            ToastUtils.showShort(R.string.disconnect_ble_success.string())
                            cancelTimer()
                            checkBind()
                        }

                        override fun onClickNo() {
                        }

                    })

            }
        })
    }

    override fun initView() {
        super.initView()
        BleSdkWrapper.init(requireContext(), object : BleCallbackWrapper() {
            override fun complete(resultCode: Int, data: Any?) {
                super.complete(resultCode, data)
                LogUtil.d("seven==> BleSdkWrapper.init() resultCode = $resultCode。。。 packageName = ${requireContext().packageName}")
                if (resultCode == Constants.BLE_RESULT_CODE.SUCCESS) {
                    //重连
                    checkBind()
                    if (BleManager.getInstance().reConnect()) {
                        showLoading()
                        timerRefreshDeviceData()
                    }

                } else {
                    LogUtil.d("SDK不能使用")
                }
            }

            override fun setSuccess() {
                Log.e("seven", "init setSuccess()")
            }
        })

        binding.refreshLayout.apply {
            setEnableRefresh(true)
            setOnRefreshListener(this@MainFragment)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        if (!BleSdkWrapper.isBind() || !BleSdkWrapper.isConnected()) {
            refreshFinish()
            return
        }
        vm.isRefreshing = true
        checkBind()
    }

    private fun refreshDeviceData() {

    }

    private fun refreshFinish() {
        vm.isRefreshing = false
        binding.refreshLayout.finishRefresh()
    }

    override fun initData() {
        super.initData()
        vm.init()
    }


    fun checkBind() {
        if (BleSdkWrapper.isBind()) {
            binding.apply {
                toolbar.mainRightTv?.text = R.string.main_unbind.string()
                toolbar.mainRightTv?.visiable()
                toolbar.mainRightIcon2?.gone()
            }
            vm.deviceStateTv.value = when (BleSdkWrapper.isConnected()) {
                true -> R.string.main_bind_device_connecr.string()
                else -> R.string.main_bind_device_unconnected.string()
            }
            if (BleSdkWrapper.isConnected()) {
                binding.refreshLayout.setEnableRefresh(true)
                vm.deviceInfoShow.value = true
                if (vm.connectedBleDevice == null) {
                    vm.connectedBleDevice = SPHelper.getBindBLEDevice(requireContext())
                }
                Prefs.bindBleDevice = BleDeviceData(false, vm.connectedBleDevice)
                Log.e("seven", "checkBind() connectedBleDevice = ${vm.connectedBleDevice}")
                if (!vm.connectedBleDevice?.mDeviceName.isNullOrEmpty()) {
                    vm.deviceProductTv.value =
                        R.string.device_name.string() + vm.connectedBleDevice?.mDeviceName
                }
                if (!vm.connectedBleDevice?.mDeviceAddress.isNullOrEmpty()) {
                    vm.deviceAddressTv.value =
                        R.string.main_mac_address.string() + vm.connectedBleDevice?.mDeviceAddress
                }
                vm.checkUploadHistoryData()
                vm.getDeviceData()
            }


        } else {
            binding.apply {
                toolbar.mainRightTv?.text = ""
                toolbar.mainRightTv?.gone()
                toolbar.mainRightIcon2?.visiable()
                refreshLayout.setEnableRefresh(false)
            }
            vm.init()
        }
    }

    private var timer: Timer? = null
    private fun timerRefreshDeviceData() {
        cancelTimer()
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                binding.root.post {
                    checkBind()
                    hideLoading()

                }
            }

        }, 5000L, 10000L)

    }

    private fun cancelTimer() {
        if (timer != null) {
            timer?.cancel()
            timer = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cancelTimer()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ConnectBleSuccessEvent) {
        vm.connectedBleDevice = event.ble
        showLoading()
        timerRefreshDeviceData()
        checkBind()
    }

}