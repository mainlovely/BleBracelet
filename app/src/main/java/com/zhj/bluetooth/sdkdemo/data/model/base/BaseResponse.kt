package com.zhj.bluetooth.sdkdemo.data.model.base

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class BaseResponse<T> : BaseModel(), Serializable {
    @SerializedName("code")
    var code: String? = ""

    @SerializedName("message")
    var message: String? = ""

    @SerializedName("data")
    var data: T? = null

    @SerializedName("success")
    var success: Boolean? = null

    @SerializedName("sign")
    var sign: String? = null
//    @SerializedName("resultCode")
//    var resultCode: String? = ""
//
//    @SerializedName("msg")
//    var msg: String? = ""
//
//    @SerializedName("data")
//    var data: T? = null
}