package com.zhj.bluetooth.sdkdemo.data.source.remote;

import com.google.gson.JsonObject;
import com.zhj.bluetooth.sdkdemo.data.model.base.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UploadBleRxApi {

    /**
     * 手环绑定接口
     */
    @FormUrlEncoded
    @POST("bracelet/bind")
    Observable<BaseResponse<Object>> braceletBind(
            @Field("DeviceProduct")String DeviceProduct,
            @Field("InterviewerId")String InterviewerId,
            @Field("VerificationCode")String VerificationCode,
            @Field("InterviewerName")String InterviewerName,
            @Field("DeviceAddress")String DeviceAddress
    );

    /**
     * 历史睡眠
     */
    @FormUrlEncoded
    @POST("bracelet/sleep")
    Observable<BaseResponse<Object>> braceletSleep(
            @Field("DeviceProduct") String DeviceProduct,
            @Field("jsonData") String jsonData
    );

    /**
     * 历史心跳
     */
    @FormUrlEncoded
    @POST("bracelet/heartbeat")
    Observable<BaseResponse<Object>> braceletHeartbeat(
            @Field("DeviceProduct") String DeviceProduct,
            @Field("jsonData") String jsonData
    );

    /**
     * 历史运动
     */
    @FormUrlEncoded
    @POST("bracelet/motion")
    Observable<BaseResponse<Object>> braceletMotion(
            @Field("DeviceProduct") String DeviceProduct,
            @Field("jsonData") String jsonData
    );
}
