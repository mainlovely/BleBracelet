package com.zhj.bluetooth.sdkdemo.ui.ble.heartrate.item

import androidx.databinding.ViewDataBinding
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.databinding.ItemHeartRateBinding
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.BaseDataBindingEpoxyModel
import com.zhj.bluetooth.sdkdemo.ui.ble.heartrate.HeartRateListVM
import com.zhj.bluetooth.sdkdemo.utils.extension.string
import com.zhj.bluetooth.zhjbluetoothsdk.bean.HealthHeartRateItem

/**
 * Created by seven on 2020/2/27
 */
@EpoxyModelClass(layout = R.layout.item_heart_rate)
abstract class ItemHeartRate : BaseDataBindingEpoxyModel() {
    @EpoxyAttribute
    lateinit var data: HealthHeartRateItem

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var vm: HeartRateListVM

    override fun onBindData(binding: ViewDataBinding, previouslyBoundModel: EpoxyModel<*>?) {
        super.onBindData(binding, previouslyBoundModel)
        (binding as ItemHeartRateBinding).apply {
            tvDate.text = "${R.string.heart_history_time.string()}${data?.year}-${data?.month}-${data?.day}-${data?.hour}:${data?.minuter}"
            tvRate.text = "${R.string.heart_history_rate.string()}${data?.heartRaveValue}"
            tvFz.text = "${R.string.heart_history_diastolic_pressure.string()}${data?.fz}"
            tvSs.text = "${R.string.heart_history_systolic_pressure.string()}${data?.ss}"
        }
    }
}