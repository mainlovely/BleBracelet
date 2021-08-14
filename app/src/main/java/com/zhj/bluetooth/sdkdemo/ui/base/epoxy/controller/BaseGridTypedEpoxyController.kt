package com.zhj.bluetooth.sdkdemo.ui.base.epoxy.controller

import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.ui.base.BaseListState
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.itemview.itemFirstLoadingGridView
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.itemview.itemGridEmptyView
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.itemview.itemNoNetworkView
import com.zhj.bluetooth.sdkdemo.utils.extension.string

abstract class BaseGridTypedEpoxyController<T>: BaseTypedEpoxyController<T>() where T: BaseListState<*> {

    override fun buildModels(data: T) {

        /**
         * 無網絡則返回 "您的网络情况不稳定", 不用走往下的邏輯
         */
        if(!com.zhj.bluetooth.sdkdemo.utils.util.NetworkUtils.isConnected()) {
            itemNoNetworkView {
                id("no_network")
                emptyView(R.mipmap.network_not_available)
                emptyText(R.string.no_network.string())
                emptyButtonText(R.string.empty_go_refresh.string())
                onClickListener(onNoNetWorkButtonClick)
            }
            return
        }

        if (data.contents.isNullOrEmpty()) {
            if(isFirstLoading) {
                /**
                 * 解決 EPOXY 很可怕的bug, 不然會永遠找不到 StaggeredGridLayoutManager.LayoutParams
                 * 一定要加開多一個 item_first_loading_grid_view
                 * 才能解決 EPOXY 本身 item view cache 的 bug..
                 */
                itemFirstLoadingGridView {
                    id("loading_view_grid")
                    loadingColor(data.itemEmptyViewData.loadingColor)
                }

                isFirstLoading = false
            } else {
                itemGridEmptyView {
                    id("empty_grid_view")
                    data(data.itemEmptyViewData)
                    onClickListener(onEmptyButtonClick)
                }
            }
        } else {
            buildContentModels(data)
        }
    }

    override fun buildContentModels(state: T) {

    }
}