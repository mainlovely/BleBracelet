package com.zhj.bluetooth.sdkdemo.data.model.base

import com.google.gson.Gson
import java.io.Serializable

open class BaseModel: Serializable{
    override fun toString() = Gson().toJson(this)
}