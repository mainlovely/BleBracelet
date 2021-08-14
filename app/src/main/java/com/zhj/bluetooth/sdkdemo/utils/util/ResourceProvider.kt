package com.zhj.bluetooth.sdkdemo.utils.util

import android.content.Context
import androidx.core.content.ContextCompat

class ResourceProvider constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: ResourceProvider? = null

        fun getInstance(context: Context) =
                INSTANCE
                    ?: synchronized(this) {
                    INSTANCE
                        ?: ResourceProvider(
                            context
                        ).also {
                        INSTANCE = it
                    }
                }
    }

    val context: Context by lazy {
        context.applicationContext // Get Application Context to avoid leaks.
    }

    fun getString(resId: Int): String {
        return context.getString(resId)
    }

    fun getString(resId: Int, value: String): String {
        return context.getString(resId, value)
    }

    fun getColor(resId: Int): Int {
        return ContextCompat.getColor(context, resId)
    }
}