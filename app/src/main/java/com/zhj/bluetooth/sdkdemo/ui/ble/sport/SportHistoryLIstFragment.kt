package com.zhj.bluetooth.sdkdemo.ui.ble.sport

import com.zhj.bluetooth.sdkdemo.ui.base.BaseListFragment
import com.zhj.bluetooth.sdkdemo.ui.ble.heartrate.item.itemHeartRate
import com.zhj.bluetooth.sdkdemo.ui.ble.sport.item.itemSport
import com.zhj.bluetooth.sdkdemo.utils.annotation.BindFragment
import com.zhj.bluetooth.sdkdemo.utils.extension.simpleController
import com.zhj.bluetooth.sdkdemo.view.CJMAppBar
import com.zhj.bluetooth.sdkdemo.view.CJMAppBarFactory
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

@BindFragment(hasRefresh = false, hasLoadMore = false)
class SportHistoryLIstFragment: BaseListFragment<SportHistoryListState, SportHistoryListVM>(){
    override fun getEpoxyController() = simpleController {state->
        state.contents.forEachIndexed { index, item ->
            itemSport {
                id("itemSportHistory_index_${index}")
                data(item)
                vm(vm)
            }
        }

    }

    override fun createViewModel() = viewModel<SportHistoryListVM>()

    override fun observeViewModel(vm: SportHistoryListVM) {
        vm.navigator = object : SportHistoryNavigator{


        }
    }

    override fun setAppBarInfo(appBar: CJMAppBar) {
        CJMAppBarFactory.create(CJMAppBarFactory.BACK_BLE_SPORT, appBar)
    }


    override fun initView() {
        super.initView()
        vm.getHistorySportData()
    }

}