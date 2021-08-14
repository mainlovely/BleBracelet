package com.zhj.bluetooth.sdkdemo.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.zhj.bluetooth.zhjbluetoothsdk.bean.BLEDevice
import com.zhj.bluetooth.sdkdemo.data.model.base.BaseModel
import com.zhj.bluetooth.zhjbluetoothsdk.bean.HealthSleepItem

/**
 * 一般来说是BaseResponse中的T泛型
 */
data class BleDeviceData(
    @SerializedName("isConnecting") var isConnecting: Boolean = false,
    @SerializedName("bleDevice") var bleDevice: BLEDevice? = null
): BaseModel()

data class HealthSleepItemCopy(
    @SerializedName("year") var year: Int = 0,
    @SerializedName("month") var month: Int = 0,
    @SerializedName("day") var day: Int = 0,
    @SerializedName("offsetMinute") var offsetMinute: Int = 0,
    @SerializedName("sleeptime") var sleeptime: String? = null,
    @SerializedName("index") var index: Int = 0,
    @SerializedName("sleepStatus") var sleepStatus: Int = 0,
    @SerializedName("date") var date: Long = 0L,
    @SerializedName("userId") var userId: String? = null,
    @SerializedName("remark") var remark: String? = null,
    @SerializedName("hour") var hour: Int = 0,
    @SerializedName("minuter") var minuter: Int = 0
): BaseModel(){
    fun convertTo(sleepData: HealthSleepItem): HealthSleepItemCopy{
        return this.apply {
            year = sleepData.year
            month = sleepData.month
            day = sleepData.day
            offsetMinute = sleepData.offsetMinute
            sleeptime = sleepData.sleeptime
            index = sleepData.index
            sleepStatus = sleepData.sleepStatus
            date = sleepData.date
            userId = sleepData.userId
            remark = sleepData.remark
            hour = sleepData.hour
            minuter = sleepData.minuter
        }
    }
}


data class HeartHistoryIsUploadSuccessData(
    @SerializedName("isUpload") var isUpload: Boolean = false,
    @SerializedName("uploadDate") var uploadDate: String? = null
): BaseModel()

data class SleepHistoryIsUploadSuccessData(
    @SerializedName("isUpload") var isUpload: Boolean = false,
    @SerializedName("uploadDate") var uploadDate: String? = null
): BaseModel()

data class SportHistoryIsUploadSuccessData(
    @SerializedName("isUpload") var isUpload: Boolean = false,
    @SerializedName("uploadDate") var uploadDate: String? = null
): BaseModel()
