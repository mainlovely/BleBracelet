package com.zhj.bluetooth.sdkdemo.data.source.repository

import com.google.gson.JsonObject
import com.zhj.bluetooth.sdkdemo.data.model.base.BaseResponse
import com.zhj.bluetooth.sdkdemo.data.model.base._Result
import com.zhj.bluetooth.sdkdemo.data.source.remote.UploadBleRxApi
import com.zhj.bluetooth.sdkdemo.utils.util.retrofit.RetrofitServiceManager
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface UploadBleRxRepository {
    suspend fun braceletBind(DeviceProduct: String, InterviewerId: String, VerificationCode: String,
                             InterviewerName: String, DeviceAddress: String, observer: Observer<BaseResponse<Any>>)
    suspend fun braceletSleep(DeviceProduct: String, jsonData: String, observer: Observer<BaseResponse<Any>>)
    suspend fun braceletHeartbeat(DeviceProduct: String, jsonData: String, observer: Observer<BaseResponse<Any>>)
    suspend fun braceletMotion(DeviceProduct: String, jsonData: String, observer: Observer<BaseResponse<Any>>)

}

class UploadBleRxRepositoryImpl: UploadBleRxRepository{
    private val uploadBleRxApi : UploadBleRxApi =
        RetrofitServiceManager.getInstance().create(UploadBleRxApi::class.java)


    override suspend fun braceletBind(DeviceProduct: String, InterviewerId: String, VerificationCode: String,
                                      InterviewerName: String, DeviceAddress: String,
                                      observer: Observer<BaseResponse<Any>>) {
        setSubscribe(uploadBleRxApi.braceletBind(DeviceProduct, InterviewerId, VerificationCode,
            InterviewerName, DeviceAddress), observer)
    }

    override suspend fun braceletSleep(
        DeviceProduct: String,
        jsonData: String,
        observer: Observer<BaseResponse<Any>>
    ) {
        setSubscribe(uploadBleRxApi.braceletSleep(DeviceProduct, jsonData), observer)
    }

    override suspend fun braceletHeartbeat(
        DeviceProduct: String,
        jsonData: String,
        observer: Observer<BaseResponse<Any>>
    ) {
        setSubscribe(uploadBleRxApi.braceletHeartbeat(DeviceProduct, jsonData), observer)
    }

    override suspend fun braceletMotion(
        DeviceProduct: String,
        jsonData: String,
        observer: Observer<BaseResponse<Any>>
    ) {
        setSubscribe(uploadBleRxApi.braceletMotion(DeviceProduct, jsonData), observer)
    }


    private fun <T>  setSubscribe(observable: Observable<T>, observer: Observer<T>){
        observable.subscribeOn(Schedulers.io())
            .subscribeOn(Schedulers.newThread())//子线程访问网络
            .observeOn(AndroidSchedulers.mainThread())//回调到主线程
            .subscribe(observer)
    }

}