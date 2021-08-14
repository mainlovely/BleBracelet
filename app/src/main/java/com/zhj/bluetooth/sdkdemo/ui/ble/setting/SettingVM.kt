package com.zhj.bluetooth.sdkdemo.ui.ble.setting

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.zhj.bluetooth.sdkdemo.data.model.base.BaseResponse
import com.zhj.bluetooth.sdkdemo.data.model.constant.RespCode
import com.zhj.bluetooth.sdkdemo.data.source.repository.UploadBleRxRepository
import com.zhj.bluetooth.sdkdemo.ui.base.BaseNavigator
import com.zhj.bluetooth.sdkdemo.ui.base.BaseViewModel
import com.zhj.bluetooth.sdkdemo.utils.extension.launch
import com.zhj.bluetooth.sdkdemo.utils.util.ToastUtils
import com.zhj.bluetooth.zhjbluetoothsdk.bean.BLEDevice
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

interface SettingNavigator : BaseNavigator {
    fun submitSuccess()
}

class SettingVM(
    private val uploadBleRxRepository: UploadBleRxRepository
) : BaseViewModel<SettingNavigator>() {
    val etName = MutableLiveData<String>()
    val etArchives = MutableLiveData<String>()
    val etCode = MutableLiveData<String>()
    var bleDevice: BLEDevice? = null

    fun onSubmit() {
        launch {
            if(bleDevice?.mDeviceName.isNullOrEmpty()){
                ToastUtils.showShort("设备号为空，请检查是否已连接手环设备")
                return@launch
            }
            if(bleDevice?.mDeviceAddress.isNullOrEmpty()){
                ToastUtils.showShort("设备蓝牙地址为空，请检查是否已连接手环设备")
                return@launch
            }
            if(etName.value.isNullOrEmpty()){
                ToastUtils.showShort("请输入姓名")
                return@launch
            }
            if(etArchives.value.isNullOrEmpty()){
                ToastUtils.showShort("请输入档案号")
                return@launch
            }
            if(etCode.value.isNullOrEmpty()){
                ToastUtils.showShort("请输入验证码")
                return@launch
            }

            Log.e("seven" ,"bleDevice = $bleDevice...InterviewerId = ${etArchives.value}...VerificationCode = ${etCode.value}...InterviewerName = ${etName.value}")
            uploadBleRxRepository.braceletBind(
                DeviceProduct = bleDevice?.mDeviceName.orEmpty(),
                InterviewerId = etArchives.value.orEmpty(),
                VerificationCode = etCode.value.orEmpty(),
                InterviewerName = etName.value.orEmpty(),
                DeviceAddress = bleDevice?.mDeviceAddress.orEmpty(),
                observer = object : Observer<BaseResponse<Any>>{
                override fun onComplete() {
                    Log.e("seven", "onComplete()")
                }

                override fun onSubscribe(p0: Disposable) {
                    Log.e("seven", "onSubscribe()")
                }

                override fun onNext(p0: BaseResponse<Any>) {
                    ToastUtils.showShort(p0.message)
                    if(p0.code == RespCode.SUCCESS){
                        navigator?.submitSuccess()
                    }
                }

                override fun onError(p0: Throwable) {
                    Log.e("seven", "onError() p0 = $p0")
                }

            })
        }
    }


}