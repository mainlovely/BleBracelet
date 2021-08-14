package com.zhj.bluetooth.sdkdemo.ui.ble.heartrate

import com.zhj.bluetooth.sdkdemo.ui.base.BaseListFragment
import com.zhj.bluetooth.sdkdemo.ui.ble.heartrate.item.itemHeartRate
import com.zhj.bluetooth.sdkdemo.utils.annotation.BindFragment
import com.zhj.bluetooth.sdkdemo.utils.extension.simpleController
import com.zhj.bluetooth.sdkdemo.view.CJMAppBar
import com.zhj.bluetooth.sdkdemo.view.CJMAppBarFactory
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

@BindFragment(hasRefresh = false, hasLoadMore = false)
class HeartRateListFragment: BaseListFragment<HeartRateListState, HeartRateListVM>(){
    override fun getEpoxyController() = simpleController {state->
        state.contents.forEachIndexed { index, item ->
            itemHeartRate {
                id("itemHeartRate_index_${index}")
                data(item)
                vm(vm)
            }
        }

    }

    override fun createViewModel() = viewModel<HeartRateListVM>()

    override fun observeViewModel(vm: HeartRateListVM) {
        vm.navigator = object : HeartRateNavigator{


        }
    }

    override fun setAppBarInfo(appBar: CJMAppBar) {
        CJMAppBarFactory.create(CJMAppBarFactory.BACK_Ble_HEART_RATE, appBar)
    }


    override fun initView() {
        super.initView()

        vm.getHistoryHeartRateData()
    }

}