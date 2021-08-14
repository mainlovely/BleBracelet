package com.zhj.bluetooth.sdkdemo.ui.ble.sport.item

import androidx.databinding.ViewDataBinding
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.databinding.ItemHeartRateBinding
import com.zhj.bluetooth.sdkdemo.databinding.ItemSportBinding
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.BaseDataBindingEpoxyModel
import com.zhj.bluetooth.sdkdemo.ui.ble.heartrate.HeartRateListVM
import com.zhj.bluetooth.sdkdemo.ui.ble.sport.SportHistoryListVM
import com.zhj.bluetooth.sdkdemo.utils.extension.string
import com.zhj.bluetooth.zhjbluetoothsdk.bean.HealthHeartRateItem
import com.zhj.bluetooth.zhjbluetoothsdk.bean.HealthSportItem

/**
 * Created by seven on 2020/2/27
 */
@EpoxyModelClass(layout = R.layout.item_sport)
abstract class ItemSport : BaseDataBindingEpoxyModel() {
    @EpoxyAttribute
    lateinit var data: HealthSportItem

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var vm: SportHistoryListVM

    override fun onBindData(binding: ViewDataBinding, previouslyBoundModel: EpoxyModel<*>?) {
        super.onBindData(binding, previouslyBoundModel)
        (binding as ItemSportBinding).apply {
            tvDate.text = "${R.string.heart_history_time.string()}${data?.year}-${data?.month}-${data?.day}-${data?.hour}:${data?.minuter}"
            tvSteps.text = "${R.string.step_history_steps.string()}${data?.stepCount}"
            tvCal.text = "${R.string.step_history_cal.string()}${data?.calory}"
            tvDistance.text = "${R.string.step_history_distance.string()}${data?.distance}"
        }
    }
}