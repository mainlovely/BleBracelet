package com.zhj.bluetooth.sdkdemo.utils.ble

import android.bluetooth.BluetoothGatt
import android.content.Context
import com.zhj.bluetooth.sdkdemo.utils.util.ToastUtils
import com.zhj.bluetooth.zhjbluetoothsdk.bean.BLEDevice
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BaseAppBleListener
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleScanTool
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleScanTool.ScanDeviceListener
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper
import com.zhj.bluetooth.zhjbluetoothsdk.util.SPHelper

interface BleHelperListener{
    fun scanBleDeviceSuccess(device: BLEDevice)
    fun scanDeviceFinish()
    fun connectBleDeviceSuccess(device: BLEDevice?)
    fun connectBleDeviceFail()
}

class BleHelper(context: Context, listener: BleHelperListener){
    private var connectDevice: BLEDevice? = null

    /**
     * 开始扫描
     */
    fun startScanBle(){
        if (BleSdkWrapper.isConnected()) {
            BleSdkWrapper.disConnect()
        }
        BleSdkWrapper.startScanDevices(scanCallback)
    }

    /**
     * 停止扫描
     */
    fun stopScanBle(){
        BleSdkWrapper.stopScanDevices()
        BleScanTool.getInstance().removeScanDeviceListener(scanCallback)
    }


    /**
     * 扫描回调
     */
    private val scanCallback: ScanDeviceListener = object : ScanDeviceListener {
        override fun onFind(device: BLEDevice) {
            listener.scanBleDeviceSuccess(device)
        }

        override fun onFinish() {
            BleSdkWrapper.stopScanDevices()
            listener.scanDeviceFinish()
        }
    }

    /**
     * 是否已连接
     */
    fun isConnected() = BleSdkWrapper.isConnected()

    /**
     * 断开连接
     */
    fun disConnect(){
        BleSdkWrapper.disConnect()
    }

    /**
     * 链接蓝牙
     */
    fun connecting(device: BLEDevice?){
        if (device == null) {
            return
        }
        connectDevice = device
        stopScanBle()
        if (BleSdkWrapper.isConnected()) {
            BleSdkWrapper.disConnect()
        }
        BleSdkWrapper.setBleListener(baseAppBleListener)
        BleSdkWrapper.connect(device)
    }

    /**
     * 连接回调
     */
    private val baseAppBleListener: BaseAppBleListener = object : BaseAppBleListener() {
        override fun onBLEConnected(bluetoothGatt: BluetoothGatt) {
            super.onBLEConnected(bluetoothGatt)
        }

        override fun initComplete() {
            super.initComplete()
            SPHelper.saveBLEDevice(context, connectDevice)
            ToastUtils.showShort("连接成功")
            listener.connectBleDeviceSuccess(connectDevice)
            BleSdkWrapper.removeListener(this)
        }

        override fun onBLEDisConnected(s: String) {
            super.onBLEDisConnected(s)
            BleSdkWrapper.removeListener(this)
            ToastUtils.showShort("连接失败")
            listener.connectBleDeviceFail()
        }

        override fun onBLEConnectTimeOut() {
            super.onBLEConnectTimeOut()
            BleSdkWrapper.removeListener(this)
            ToastUtils.showShort("连接超时")
            listener.connectBleDeviceFail()
        }
    }

    /**
     * 移除监听
     */
    fun removeListener(){
        BleSdkWrapper.removeListener(baseAppBleListener)
        BleScanTool.getInstance().removeScanDeviceListener(scanCallback)
    }
}