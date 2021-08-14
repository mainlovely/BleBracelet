package com.zhj.bluetooth.sdkdemo.ui.ble.sleep

import com.zhj.bluetooth.sdkdemo.ui.base.BaseListFragment
import com.zhj.bluetooth.sdkdemo.ui.ble.sleep.item.itemSleep
import com.zhj.bluetooth.sdkdemo.utils.annotation.BindFragment
import com.zhj.bluetooth.sdkdemo.utils.extension.simpleController
import com.zhj.bluetooth.sdkdemo.view.CJMAppBar
import com.zhj.bluetooth.sdkdemo.view.CJMAppBarFactory
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

@BindFragment(hasRefresh = false, hasLoadMore = false)
class SleepListFragment: BaseListFragment<SleepListState, SleepListVM>(){
    override fun getEpoxyController() = simpleController {state ->
        state.contents.forEachIndexed { index, item ->
            itemSleep {
                id("itemHeartRate_index_${index}")
                data(item)
                vm(vm)
            }
        }
    }

    override fun createViewModel() = viewModel<SleepListVM>()

    override fun observeViewModel(vm: SleepListVM) {
        vm.navigator = object : SleepListNavigator {
        }
    }

    override fun setAppBarInfo(appBar: CJMAppBar) {
        CJMAppBarFactory.create(CJMAppBarFactory.BACK_Ble_SLEEP, appBar)
    }

    override fun initView() {
        super.initView()
        vm.getHistoryHeartRateData()
    }
}