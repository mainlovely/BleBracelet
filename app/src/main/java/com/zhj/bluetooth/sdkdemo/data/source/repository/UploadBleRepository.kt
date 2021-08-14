package com.zhj.bluetooth.sdkdemo.data.source.repository

import com.zhj.bluetooth.sdkdemo.data.model.base._Result
import com.zhj.bluetooth.sdkdemo.data.source.remote.UploadBleApi
import com.zhj.bluetooth.sdkdemo.utils.extension.apiCall

interface UploadBleRepository{
    suspend fun braceletBind(DeviceProduct: String, InterviewerId: String, VerificationCode: String,
                             InterviewerName: String, DeviceAddress: String): _Result<Any>
    suspend fun braceletSleep(DeviceProduct: String, jsonData: Any): _Result<Any>
    suspend fun braceletHeartbeat(DeviceProduct: String, jsonData: Any): _Result<Any>
    suspend fun braceletMotion(DeviceProduct: String, jsonData: Any): _Result<Any>
}

class UploadBleRepositoryImpl(private val uploadBleApi: UploadBleApi): UploadBleRepository{


    override suspend fun braceletBind(
        DeviceProduct: String,
        InterviewerId: String,
        VerificationCode: String,
        InterviewerName: String,
        DeviceAddress: String
    )  = apiCall { uploadBleApi.braceletBind(DeviceProduct, InterviewerId, VerificationCode, InterviewerName, DeviceAddress) }

    override suspend fun braceletSleep(DeviceProduct: String, jsonData: Any) =
        apiCall { uploadBleApi.braceletSleep(DeviceProduct, jsonData) }

    override suspend fun braceletHeartbeat(DeviceProduct: String, jsonData: Any)=
        apiCall { uploadBleApi.braceletHeartbeat(DeviceProduct, jsonData) }

    override suspend fun braceletMotion(DeviceProduct: String, jsonData: Any)=
        apiCall { uploadBleApi.braceletMotion(DeviceProduct, jsonData) }



}