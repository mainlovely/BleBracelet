package com.zhj.bluetooth.sdkdemo.ui.base.epoxy.controller

import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import com.zhj.bluetooth.sdkdemo.ui.base.BaseListState
import com.zhj.bluetooth.sdkdemo.utils.extension.log
import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.itemview.itemEmptyView
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.itemview.itemFirstLoadingView
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.itemview.itemNoNetworkView
import com.zhj.bluetooth.sdkdemo.utils.extension.string

abstract class BaseTypedEpoxyController<T>: TypedEpoxyController<T>() where T: BaseListState<*> {

    var onEmptyButtonClick: View.OnClickListener? = null
    var onNoNetWorkButtonClick: View.OnClickListener? = null
    protected var isFirstLoading = true

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
            log("buildModels 2")
            if(isFirstLoading) {
                itemFirstLoadingView {
                    id("loading_view")
                    loadingColor(data.itemEmptyViewData.loadingColor)
                    spanSizeOverride { spanSize, _, _ -> spanSize }
                }

                isFirstLoading = false
            } else {
                buildEmptyWithHeader(data)

                itemEmptyView {
                    id("empty_view")
                    data(data.itemEmptyViewData)
                    onClickListener(onEmptyButtonClick)
                    // support grid layout
                    spanSizeOverride { spanSize, _, _ -> spanSize }
                }

                buildEmptyWithFooter(data)
            }
        } else {
            buildContentModels(data)
        }
    }

    open fun buildEmptyWithHeader(data: T) {}
    open fun buildEmptyWithFooter(data: T) {}

    open fun buildContentModels(state: T) {}
}