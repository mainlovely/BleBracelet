package com.zhj.bluetooth.sdkdemo.data.source.remote

import com.zhj.bluetooth.sdkdemo.data.model.base.BaseResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UploadBleApi {
    /**
     * 手环绑定接口
     */
    @FormUrlEncoded
    @POST("/bracelet/bind")
    suspend fun braceletBind(
        @Field("DeviceProduct") DeviceProduct: String,
        @Field("InterviewerId") InterviewerId: String,
        @Field("VerificationCode") VerificationCode: String,
        @Field("InterviewerName") InterviewerName: String,
        @Field("DeviceAddress") DeviceAddress: String
    ): BaseResponse<Any>

    /**
     * 历史睡眠
     */
    @FormUrlEncoded
    @POST("/bracelet/sleep")
    suspend fun braceletSleep(
        @Field("DeviceProduct") DeviceProduct: String,
        @Field("jsonData") jsonData: Any
    ): BaseResponse<Any>

    /**
     * 历史心跳
     */
    @FormUrlEncoded
    @POST("/bracelet/heartbeat")
    suspend fun braceletHeartbeat(
        @Field("DeviceProduct") DeviceProduct: String,
        @Field("jsonData") jsonData: Any
    ): BaseResponse<Any>

    /**
     * 历史运动
     */
    @FormUrlEncoded
    @POST("/bracelet/motion")
    suspend fun braceletMotion(
        @Field("DeviceProduct") DeviceProduct: String,
        @Field("jsonData") jsonData: Any
    ): BaseResponse<Any>

}