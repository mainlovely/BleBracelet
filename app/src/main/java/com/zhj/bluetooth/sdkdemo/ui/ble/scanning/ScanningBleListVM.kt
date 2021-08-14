package com.zhj.bluetooth.sdkdemo.ui.ble.scanning

import com.zhj.bluetooth.sdkdemo.data.model.BleDeviceData
import com.zhj.bluetooth.sdkdemo.data.model.constant.LoadState
import com.zhj.bluetooth.sdkdemo.ui.base.BaseListState
import com.zhj.bluetooth.sdkdemo.ui.base.BaseListViewModel
import com.zhj.bluetooth.sdkdemo.ui.base.BaseNavigator
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.itemview.ItemEmptyViewFactory
import com.zhj.bluetooth.sdkdemo.utils.extension.launch
import com.zhj.bluetooth.sdkdemo.utils.extension.updateOne
import com.zhj.bluetooth.zhjbluetoothsdk.bean.BLEDevice

interface ScanningBleNavigator: BaseNavigator {
    fun onItemClick(data: BleDeviceData?)

}

data class ScanningBleListState(
    override var contents: List<BleDeviceData> = listOf()
): BaseListState<BleDeviceData>()

class ScanningBleListVM: BaseListViewModel<ScanningBleListState, ScanningBleNavigator>() {
    override val listState = ScanningBleListState()

    override fun onFetchData(loadingState: Int) {
        super.onFetchData(loadingState)
        launch {

        }
    }

    override fun setEmptyViewState() {
        state.itemEmptyViewData = ItemEmptyViewFactory.create(ItemEmptyViewFactory.NO_SEARCH_RESULT)
    }

    fun clearContents(){
        setState(LoadState.UPDATE_ITEM){
            state.apply {
                contents = listOf()
            }
        }
    }

    fun scanUpdateContents(device: BLEDevice){
        val list: MutableList<BleDeviceData> = state.contents.toMutableList()

        if(list.none { it.bleDevice?.mDeviceName == device.mDeviceName }){
            list.add(BleDeviceData(bleDevice = device))
            setState(LoadState.UPDATE_ITEM){
                state.apply {
                    contents = list.toList()
                }
            }

        }
    }

    fun onItemClick(data: BleDeviceData?){
        navigator?.onItemClick(data)
    }

    fun updateConnecting(device: BleDeviceData){
        setState(LoadState.UPDATE_ITEM){
            state.apply {
                contents = contents.updateOne(
                    finder = { device.bleDevice?.mDeviceAddress == it.bleDevice?.mDeviceAddress },
                    action = { isConnecting = true }
                )
            }
        }
    }

    fun updateConnectFail(){
        setState(LoadState.UPDATE_ITEM){
            state.apply {
                contents = contents.updateOne(
                    finder = { it.isConnecting },
                    action = { isConnecting = false }
                )
            }
        }
    }
}