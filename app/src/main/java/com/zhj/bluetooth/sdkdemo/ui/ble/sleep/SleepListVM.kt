package com.zhj.bluetooth.sdkdemo.ui.ble.sleep

import android.util.Log
import com.zhj.bluetooth.sdkdemo.data.model.constant.LoadState
import com.zhj.bluetooth.sdkdemo.ui.base.BaseListState
import com.zhj.bluetooth.sdkdemo.ui.base.BaseListViewModel
import com.zhj.bluetooth.sdkdemo.ui.base.BaseNavigator
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.itemview.ItemEmptyViewFactory
import com.zhj.bluetooth.sdkdemo.utils.extension.launch
import com.zhj.bluetooth.zhjbluetoothsdk.bean.HealthSleepItem
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleCallback
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult
import java.util.*

interface SleepListNavigator : BaseNavigator {
}

data class SleepListState(
    override var contents: List<HealthSleepItem> = listOf()
) : BaseListState<HealthSleepItem>()

class SleepListVM : BaseListViewModel<SleepListState, SleepListNavigator>() {
    override val listState = SleepListState()
    private val allHealthSleepList = mutableListOf<HealthSleepItem>()
    private val calendar: Calendar by lazy { Calendar.getInstance() }

    override fun setEmptyViewState() {
        state.itemEmptyViewData =
            ItemEmptyViewFactory.create(ItemEmptyViewFactory.NO_SLEEP_RESULT)
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
        BleSdkWrapper.getStepOrSleepHistory(year, month, day, object : BleCallback {
            override fun complete(resultCode: Int, data: Any) {
                if (data is HandlerBleDataResult) {
                    val result = data as HandlerBleDataResult
                    if (result.sleepItems is List<*>) {
//                        Log.e(
//                            "seven",
//                            "获取历史睡眠数据: result.isComplete = ${result.isComplete}... result.hasNext = ${result.hasNext}...result.sleep = ${result.sleepItems}... "
//                        )
                        result.sleepItems.forEachIndexed { index, healthSleepItem ->
                            Log.e(
                                "seven",
                                "获取历史睡眠数据:healthSleepItem = ${healthSleepItem.date}...offsetMinute = ${healthSleepItem.offsetMinute}... sleepStatus = ${healthSleepItem.sleepStatus}...sleeptime = ${healthSleepItem.sleeptime}"
                            )
                        }

                        if (result.isComplete) {
                            if (result.hasNext) { //是否还有更多的历史数据
                                val healthSleepItems = result.sleepItems as List<HealthSleepItem>
                                allHealthSleepList.addAll(healthSleepItems)
                                setState(LoadState.UPDATE_ITEM) {
                                    state.apply {
                                        contents = allHealthSleepList
                                    }
                                }
                                calendar.add(Calendar.DATE, -1)
                                getData()
                            } else {
//                            setState(LoadState.UPDATE_ITEM) {
//                                state.apply {
//                                    contents = allHealthSleepList.toList()
//                                }
//                            }

                            }
                        }
                    }

                }
            }

            override fun setSuccess() {}
        })
    }

}