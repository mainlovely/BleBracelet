package com.zhj.bluetooth.sdkdemo.ui.ble.heartrate

import android.util.Log
import com.zhj.bluetooth.sdkdemo.data.model.constant.LoadState
import com.zhj.bluetooth.sdkdemo.ui.base.BaseListState
import com.zhj.bluetooth.sdkdemo.ui.base.BaseListViewModel
import com.zhj.bluetooth.sdkdemo.ui.base.BaseNavigator
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.itemview.ItemEmptyViewFactory
import com.zhj.bluetooth.sdkdemo.utils.extension.launch
import com.zhj.bluetooth.zhjbluetoothsdk.bean.HealthHeartRateItem
import com.zhj.bluetooth.zhjbluetoothsdk.bean.HealthSleepItem
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleCallback
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult
import java.util.*

interface HeartRateNavigator : BaseNavigator {
}

data class HeartRateListState(
    override var contents: List<HealthHeartRateItem> = listOf()
) : BaseListState<HealthHeartRateItem>()

class HeartRateListVM : BaseListViewModel<HeartRateListState, HeartRateNavigator>() {
    override val listState = HeartRateListState()
    private val allHeartRateList = mutableListOf<HealthHeartRateItem>()
    private val calendar: Calendar by lazy { Calendar.getInstance() }

    override fun setEmptyViewState() {
        state.itemEmptyViewData =
            ItemEmptyViewFactory.create(ItemEmptyViewFactory.NO_HEART_RATE_RESULT)
    }

    fun getHistoryHeartRateData() {
        launch {
            getData()
        }
    }

    private fun getData() {
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH] + 1
        val day = calendar[Calendar.DATE]
        BleSdkWrapper.getHistoryHeartRateData(year, month, day, object : BleCallback {
            override fun complete(resultCode: Int, data: Any) {
                if (data is HandlerBleDataResult) {
                    val result = data as HandlerBleDataResult
                    if (result.data is List<*>) {
                        Log.e(
                            "seven",
                            "获取历史心跳数据: result.isComplete = ${result.isComplete}... result.hasNext = ${result.hasNext}...result = $result... "
                        )
                        if (result.isComplete) {
                            if (result.hasNext) { //是否还有更多的历史数据
                                val healthHeartRateItems = result.data as List<HealthHeartRateItem>
                                allHeartRateList.addAll(healthHeartRateItems)
                                setState(LoadState.UPDATE_ITEM) {
                                    state.apply {
                                        contents = allHeartRateList
                                    }
                                }
                                calendar.add(Calendar.DATE, -1)
                                getData()
                            } else {
//                                setState(LoadState.UPDATE_ITEM) {
//                                    state.apply {
//                                        contents = allHeartRateList.toList()
//                                    }
//                                }

                            }
                        }
                    }

                }
            }

            override fun setSuccess() {}
        })
    }

}