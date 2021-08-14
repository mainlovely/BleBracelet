package com.zhj.bluetooth.sdkdemo.ui.ble.sport

import android.util.Log
import com.zhj.bluetooth.sdkdemo.data.model.constant.LoadState
import com.zhj.bluetooth.sdkdemo.ui.base.BaseListState
import com.zhj.bluetooth.sdkdemo.ui.base.BaseListViewModel
import com.zhj.bluetooth.sdkdemo.ui.base.BaseNavigator
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.itemview.ItemEmptyViewFactory
import com.zhj.bluetooth.sdkdemo.utils.extension.launch
import com.zhj.bluetooth.zhjbluetoothsdk.bean.HealthHeartRateItem
import com.zhj.bluetooth.zhjbluetoothsdk.bean.HealthSleepItem
import com.zhj.bluetooth.zhjbluetoothsdk.bean.HealthSportItem
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleCallback
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult
import java.util.*

interface SportHistoryNavigator : BaseNavigator {
}

data class SportHistoryListState(
    override var contents: List<HealthSportItem> = listOf()
) : BaseListState<HealthSportItem>()

class SportHistoryListVM : BaseListViewModel<SportHistoryListState, SportHistoryNavigator>() {
    override val listState = SportHistoryListState()
    private val allSportList = mutableListOf<HealthSportItem>()
    private val calendar: Calendar by lazy { Calendar.getInstance() }

    override fun setEmptyViewState() {
        state.itemEmptyViewData =
            ItemEmptyViewFactory.create(ItemEmptyViewFactory.NO_SPORT_RESULT)
    }

    fun getHistorySportData() {
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
                    if (result.data is List<*>) {
                        Log.e(
                            "seven",
                            "获取历史运动数据: result.isComplete = ${result.isComplete}... result.hasNext = ${result.hasNext}...result = $result... "
                        )
                        if (result.isComplete) {
                            if (result.hasNext) { //是否还有更多的历史数据
                                val sportItems = result.data as List<HealthSportItem>
                                allSportList.addAll(sportItems)
                                setState(LoadState.UPDATE_ITEM) {
                                    state.apply {
                                        contents = allSportList
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