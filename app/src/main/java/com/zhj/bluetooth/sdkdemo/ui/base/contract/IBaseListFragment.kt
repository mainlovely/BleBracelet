package com.zhj.bluetooth.sdkdemo.ui.base.contract

import com.airbnb.epoxy.EpoxyRecyclerView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.zhj.bluetooth.sdkdemo.ui.base.BaseListState
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.controller.BaseTypedEpoxyController

interface IBaseListFragment<S : BaseListState<*>>: OnRefreshListener, OnLoadMoreListener {
    /**
     * 自定義 controller / EpoxyRecyclerView
     */
    fun getEpoxyController(): BaseTypedEpoxyController<S>
    fun setEpoxyRecyclerView(recyclerView: EpoxyRecyclerView?)

    /**
    * RecyclerView 空數據時中間的按鈕處理
    */
    fun onClickItemEmptyButton()

    /**
     * RecyclerView 无网络中間的按鈕處理
     */
    fun onClickItemNetErrorButton()

    /**
     * RefreshLayout 下拉數據刷新處理
     */
    override fun onRefresh(refreshLayout: RefreshLayout)

    /**
     * RefreshLayout 上拉數據刷新處理
     */
    override fun onLoadMore(refreshLayout: RefreshLayout)

    /**
     * RefreshLayout 上拉或下拉數據刷新處理
     */
    fun onLoadData(isRefresh: Boolean = false)
}