package com.zhj.bluetooth.sdkdemo.ui.ble.scanning.item

import android.view.animation.AnimationUtils
import androidx.databinding.ViewDataBinding
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.data.model.BleDeviceData
import com.zhj.bluetooth.sdkdemo.databinding.ItemScanningBleBinding
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.BaseDataBindingEpoxyModel
import com.zhj.bluetooth.sdkdemo.ui.ble.scanning.ScanningBleListVM
import com.zhj.bluetooth.sdkdemo.utils.extension.gone
import com.zhj.bluetooth.sdkdemo.utils.extension.visiable
import kotlin.math.abs

/**
 * Created by seven on 2020/2/27
 */
@EpoxyModelClass(layout = R.layout.item_scanning_ble)
abstract class ItemScanningBle : BaseDataBindingEpoxyModel() {
    @EpoxyAttribute
    lateinit var data: BleDeviceData

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var vm: ScanningBleListVM

    override fun onBindData(binding: ViewDataBinding, previouslyBoundModel: EpoxyModel<*>?) {
        super.onBindData(binding, previouslyBoundModel)
        (binding as ItemScanningBleBinding).apply {
            when (data?.isConnecting) {
                true -> {
                    tvState.gone()
                    tvConnect.visiable()
                    tvConnect.animation =
                        AnimationUtils.loadAnimation(root.context, R.anim.progress_drawable)
                }
                else -> {
                    tvState.visiable()
                    tvConnect.gone()
                    tvState.setImageResource(when {
                        abs(data?.bleDevice?.mRssi?:0) <= 70 -> {
                            R.mipmap.device_rssi_1
                        }
                        abs(data?.bleDevice?.mRssi?:0) <= 90 -> {
                            R.mipmap.device_rssi_2
                        }
                        else -> {
                            R.mipmap.device_rssi_3
                        }
                    })
                }
            }
        }
    }
}