package com.zhj.bluetooth.sdkdemo.ui.ble.sleep.item

import androidx.databinding.ViewDataBinding
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.databinding.ItemSleepBinding
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.BaseDataBindingEpoxyModel
import com.zhj.bluetooth.sdkdemo.ui.ble.sleep.SleepListVM
import com.zhj.bluetooth.sdkdemo.utils.extension.string
import com.zhj.bluetooth.zhjbluetoothsdk.bean.HealthSleepItem

/**
 * Created by seven on 2020/2/27
 */
@EpoxyModelClass(layout = R.layout.item_sleep)
abstract class ItemSleep : BaseDataBindingEpoxyModel() {
    @EpoxyAttribute
    lateinit var data: HealthSleepItem

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var vm: SleepListVM

    override fun onBindData(binding: ViewDataBinding, previouslyBoundModel: EpoxyModel<*>?) {
        super.onBindData(binding, previouslyBoundModel)
        (binding as ItemSleepBinding).apply {
            tvDate.text =
                "${R.string.heart_history_time.string()}${data?.year}-${data?.month}-${data?.day}-${data?.hour}:${data?.minuter}"
            //0x01:开始入睡 0x02:浅睡 0x03:深睡 0x04:清醒 0x05:快速眼动睡眠
            tvSleepState.text =
                "${R.string.sleep_history_status.string()}${when (data?.sleepStatus) {
                    1 -> "开始入睡"
                    2 -> "浅睡"
                    3 -> "深睡"
                    4 -> "清醒"
                    5 -> "快速眼动睡眠"
                    else -> "其他"
                }
                }"
            tvSleepTime.text =
                "${R.string.sleep_history_time.string()}${when (data?.sleepStatus) {
                    1, 2, 3, 4, 5 -> data?.offsetMinute
                    else -> when (data?.sleeptime.isNullOrEmpty()) {
                        true -> 0
                        else -> data?.sleeptime
                    }
                }
                }"
//                "${R.string.sleep_history_time.string()}${when (data?.sleeptime.isNullOrEmpty()) {
//                    true -> 0
//                    else -> data?.sleeptime
//                }
//                }"
        }
    }
}