package com.zhj.bluetooth.sdkdemo.ui.ble.scanning

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.View
import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.data.model.BleDeviceData
import com.zhj.bluetooth.sdkdemo.data.model.ConnectBleSuccessEvent
import com.zhj.bluetooth.sdkdemo.ui.base.BaseListFragment
import com.zhj.bluetooth.sdkdemo.ui.base.dialog.ConfirmDialogFragment
import com.zhj.bluetooth.sdkdemo.ui.ble.scanning.item.itemScanningBle
import com.zhj.bluetooth.sdkdemo.utils.annotation.BindFragment
import com.zhj.bluetooth.sdkdemo.utils.ble.BleHelper
import com.zhj.bluetooth.sdkdemo.utils.ble.BleHelperListener
import com.zhj.bluetooth.sdkdemo.utils.extension.*
import com.zhj.bluetooth.sdkdemo.utils.util.CommonUtil
import com.zhj.bluetooth.sdkdemo.utils.util.PermissionUtils
import com.zhj.bluetooth.sdkdemo.utils.util.ToastUtils
import com.zhj.bluetooth.sdkdemo.view.CJMAppBar
import com.zhj.bluetooth.sdkdemo.view.CJMAppBarFactory
import com.zhj.bluetooth.zhjbluetoothsdk.bean.BLEDevice
import com.zhj.bluetooth.zhjbluetoothsdk.bean.HealthHeartRate
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleCallback
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleScanTool
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.viewModel


const val REQUEST_LOCATION_CODE = 1000
const val REQUEST_OPEN_BLE_CODE = 1001
@BindFragment(hasRefresh = false, hasLoadMore = false)
class ScanningBleListFragment: BaseListFragment<ScanningBleListState, ScanningBleListVM>(), BleHelperListener {
    private var bleHelper: BleHelper? = null
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var isConnecting = false

    override fun getEpoxyController() = simpleController{state ->
        state.contents.forEachIndexed { index, data ->
            itemScanningBle {
                id("itemScanningBle_${index}_${data.bleDevice?.mDeviceName}")
                data(data)
                vm(vm)
            }
        }
    }
    override fun createViewModel() = viewModel<ScanningBleListVM>()

    override fun observeViewModel(vm: ScanningBleListVM) {
        vm.navigator = object :
            ScanningBleNavigator {
            override fun onItemClick(data: BleDeviceData?) {
                if(!BleScanTool.getInstance().isBluetoothOpen){
                    ToastUtils.showShort(R.string.scan_device_blu_not_open)
                    return
                }
                if(!isConnecting){
                    isConnecting = true
                    data?.let {
                        vm.updateConnecting(it)
                        bleHelper?.connecting(it.bleDevice)
                    }
                }

            }

        }
    }

    override fun setAppBarInfo(appBar: CJMAppBar) {
        CJMAppBarFactory.create(CJMAppBarFactory.BACK_SCAN, appBar)
    }

    override fun onClickItemEmptyButton() {
        getScanDevice()
    }

    override fun initView() {
        super.initView()
        bleHelper = BleHelper(requireContext(), this)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        openBle()
    }

    private fun openBle(){
        if(bluetoothAdapter?.isEnabled == false){
            startActivityForResult(Intent().apply {
                action = BluetoothAdapter.ACTION_REQUEST_ENABLE
            }, REQUEST_OPEN_BLE_CODE)
        }else{
            openGPS()
        }
    }

    private fun openGPS(){
        if(!CommonUtil.isOPen(requireContext())){
            popConfirmDialog(title = R.string.permisson_location_tips.string(), clickOut = false, listener = object : ConfirmDialogFragment.ConfirmDialogListener{
                override fun onClickYes() {
                    startActivityForResult(Intent().apply {
                        action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    }, REQUEST_LOCATION_CODE)
                }

                override fun onClickNo() {
                }
            })
        }else{
            getScanDevice()
        }
    }

    private fun getScanDevice(){
        vm.clearContents()
        PermissionUtils.checkHavePermission(requireContext(), PermissionUtils.LOCATION_PERMISSION,
            object : PermissionUtils.PermissionListener{
                override fun onSuccess() {
                    super.onSuccess()
                    if(bleHelper?.isConnected() == true){
                        bleHelper?.disConnect()
                    }
                    bleHelper?.startScanBle()
                    showLoading()
                }

                override fun onFail() {
                    super.onFail()
                }

        })
    }


    override fun scanBleDeviceSuccess(device: BLEDevice) {
        hideLoading()
        vm.scanUpdateContents(device)
    }

    override fun scanDeviceFinish() {
        hideLoading()
    }

    override fun connectBleDeviceSuccess(device: BLEDevice?) {
        isConnecting = false
        vm.updateConnectFail()
        EventBus.getDefault().post(ConnectBleSuccessEvent(device))
        selfDestroy()
    }

    override fun connectBleDeviceFail() {
        isConnecting = false
        vm.updateConnectFail()
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK){
            return
        }
        when(requestCode){
            REQUEST_LOCATION_CODE -> getScanDevice()
            REQUEST_OPEN_BLE_CODE -> openGPS()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bluetoothAdapter = null
        bleHelper?.removeListener()
    }

}