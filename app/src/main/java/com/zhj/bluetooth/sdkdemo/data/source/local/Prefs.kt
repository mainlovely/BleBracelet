package com.zhj.bluetooth.sdkdemo.data.source.local

import android.util.Log
import com.zhj.bluetooth.sdkdemo.data.model.BleDeviceData
import com.zhj.bluetooth.sdkdemo.data.model.HeartHistoryIsUploadSuccessData
import com.zhj.bluetooth.sdkdemo.data.model.SleepHistoryIsUploadSuccessData
import com.zhj.bluetooth.sdkdemo.data.model.SportHistoryIsUploadSuccessData
import com.zhj.bluetooth.sdkdemo.data.model.base.BaseModel
import com.zhj.bluetooth.sdkdemo.utils.util.GsonUtils
import com.zhj.bluetooth.sdkdemo.utils.util.SPStaticUtils
import com.zhj.bluetooth.zhjbluetoothsdk.bean.BLEDevice


object Prefs {
    var bindBleDevice: BleDeviceData?
        get() = getObject(SP_BIND_BLE_DEVICE, BleDeviceData::class.java)
        set(value) = put(SP_BIND_BLE_DEVICE, value)

    var heartUpload: HeartHistoryIsUploadSuccessData?
        get() = getObject(SP_UPLOAD_HEART_HISTORY, HeartHistoryIsUploadSuccessData::class.java)
        set(value) = put(SP_UPLOAD_HEART_HISTORY, value)

    var sleepUpload: SleepHistoryIsUploadSuccessData?
        get() = getObject(SP_UPLOAD_SLEEP_HISTORY, SleepHistoryIsUploadSuccessData::class.java)
        set(value) = put(SP_UPLOAD_SLEEP_HISTORY, value)

    var sportUpload: SportHistoryIsUploadSuccessData?
        get() = getObject(SP_UPLOAD_SPORT_HISTORY, SportHistoryIsUploadSuccessData::class.java)
        set(value) = put(SP_UPLOAD_SPORT_HISTORY, value)

    private fun <T> put(key: String, value: T?) {
        try {
            if (value != null) {
                when (value) {
                    is Boolean -> SPStaticUtils.put(key, value)
                    is Int -> SPStaticUtils.put(key, value)
                    is Float -> SPStaticUtils.put(key, value)
                    is Long -> SPStaticUtils.put(key, value)
                    is String -> SPStaticUtils.put(key, value)
                    is BaseModel -> SPStaticUtils.put(key, GsonUtils.toJson(value)
                    )
                    else -> throw Exception("following class is not support")
                }
            }
        } catch (ex: Exception) {
            Log.e("Prefs", "$key put value fail $ex")
        }
    }

    private fun <T> getObject(key: String, classOfT: Class<T>): T? {
        return try {
            if (SPStaticUtils.contains(key))
                GsonUtils.fromJson(
                    SPStaticUtils.getString(key), classOfT
                )
            else
                null
        } catch (ex: Exception) {
            Log.e("Prefs", "$key get value fail $ex")
            null
        }
    }

    private fun <T> getObject(key: String, classOfT: Class<T>, defaultValue: T): T {
        return try {
            if (SPStaticUtils.contains(key))
                GsonUtils.fromJson(
                    SPStaticUtils.getString(key), classOfT
                )
            else
                defaultValue
        } catch (ex: Exception) {
            Log.e("Prefs", "$key get value fail $ex")
            defaultValue
        }
    }

    private const val SP_BIND_BLE_DEVICE = "SP_BIND_BLE_DEVICE"
    private const val SP_UPLOAD_HEART_HISTORY = "SP_UPLOAD_HEART_HISTORY"
    private const val SP_UPLOAD_SLEEP_HISTORY = "SP_UPLOAD_SLEEP_HISTORY"
    private const val SP_UPLOAD_SPORT_HISTORY = "SP_UPLOAD_SPORT_HISTORY"

}