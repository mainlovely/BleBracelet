package com.zhj.bluetooth.sdkdemo.ui.base.epoxy.itemview

import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.data.model.base.BaseModel
import com.zhj.bluetooth.sdkdemo.utils.extension.string


object ItemEmptyViewFactory {
    const val NO_SEARCH_RESULT = 1
    const val NO_HEART_RATE_RESULT = 2
    const val NO_SLEEP_RESULT = 3
    const val NO_SPORT_RESULT = 4



    fun create(type: Int = 0) = when (type) {

        NO_SEARCH_RESULT -> ItemEmptyViewData(R.mipmap.no_result, R.string.no_search_ble_device.string(), R.string.empty_go_scanning.string())
        NO_HEART_RATE_RESULT -> ItemEmptyViewData(R.mipmap.no_result, R.string.no_search_ble_heart_rate.string())
        NO_SLEEP_RESULT -> ItemEmptyViewData(R.mipmap.no_result, R.string.no_search_ble_sleep.string())
        NO_SPORT_RESULT -> ItemEmptyViewData(R.mipmap.no_result, R.string.no_search_ble_sport.string())
        else -> ItemEmptyViewData(R.mipmap.no_result, R.string.no_search_result.string(), R.string.empty_go_refresh.string())
    }
}

data class ItemEmptyViewData(
    var emptyImage: Int = R.mipmap.no_result,
    var emptyText: String = R.string.no_search_result.string(),
    var emptyButtonText: String = R.string.empty_go_refresh.string(),
    var textColor: Int = R.color.white,
    var loadingColor: Int = R.color.colorPrimaryRed
) : BaseModel()