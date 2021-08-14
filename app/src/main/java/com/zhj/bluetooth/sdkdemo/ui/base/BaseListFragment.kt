package com.zhj.bluetooth.sdkdemo.ui.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyRecyclerView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.zhj.bluetooth.sdkdemo.data.model.constant.LoadState
import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.controller.BaseTypedEpoxyController
import com.zhj.bluetooth.sdkdemo.ui.base.contract.IBaseListFragment
import com.zhj.bluetooth.sdkdemo.utils.annotation.BindFragment
import com.zhj.bluetooth.sdkdemo.utils.extension.color


const val FIRST_PAGE_NO = 1
const val DEFAULT_PAGE_SIZE = 10

abstract class BaseListFragment<
        S : BaseListState<*>,
        V : BaseListViewModel<S, *>>
    : BaseFragment<ViewDataBinding, V>(), IBaseListFragment<S> {

    companion object {
        const val TAG = "BaseListFragment"
    }

    override val tagClass = TAG

    private var needRefresh = true
    private var needLoadMore = true
    private var pageSize = DEFAULT_PAGE_SIZE
    private var isFirstLoad = false
    private var hasCoord = false
    private var hasTransToolbar = false

    private var currentContentSize = 0

    protected var recyclerView: EpoxyRecyclerView? = null
    protected var smartRefreshLayout: SmartRefreshLayout? = null
    protected var coordinatorLayout: CoordinatorLayout? = null
    protected val controller: BaseTypedEpoxyController<S>? by lazy { getEpoxyController() }

    abstract override fun getEpoxyController(): BaseTypedEpoxyController<S>

    override fun onLoadData(isRefresh: Boolean) {
        vm.smartRefreshState = if (isRefresh) LoadState.REFRESHING else LoadState.LOADING
        vm.onFetchData(vm.smartRefreshState)
    }

    override fun onClickItemEmptyButton() {}
    override fun onClickItemNetErrorButton() {}
    override fun setEpoxyRecyclerView(recyclerView: EpoxyRecyclerView?) {}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view, savedInstanceState)
    }

    private fun init(view: View, savedInstanceState: Bundle?) {
        initData()
        vm.setEmptyViewState()

        recyclerView = view.findViewById(R.id.base_recycler_view)
        smartRefreshLayout = view.findViewById(R.id.base_refresh_layout)
        coordinatorLayout = view.findViewById(R.id.base_coordinator_layout)

        if (controller == null) {
            throw Exception("Please make sure has EpoxyController.")
        }

        if (recyclerView == null) {
            throw Exception("Please make sure has R.id.base_recycler_view in your xml.")
        }

        if (smartRefreshLayout == null) {
            throw Exception("Please make sure has R.id.base_refresh_layout in your xml.")
        }

        if (hasCoord && coordinatorLayout == null) {
            throw Exception("Please make sure has R.id.base_coordinator_layout in your xml.")
        }

        setEpoxyRecyclerView(recyclerView)

        recyclerView?.setController(controller!!)

        if (hasTransToolbar) {
            recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                private var totalDy = 0

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    totalDy += dy
                    if (totalDy < 0) {
                        totalDy = 0
                    }

                    if (totalDy < 500) {
                        val alpha: Float = totalDy.toFloat() / 500
                        appBar?.setBackgroundColor(getColorWithAlpha(R.color.black.color(), alpha))
                    } else {
                        appBar?.setBackgroundColor(R.color.black.color())
                    }
                }
            })
        }


        // List items 處理
        if (!vm.items.hasObservers()) {
            vm.items.observe(activity!!, Observer { data ->
                when (smartRefreshLayout?.state) {
                    // 上拉刷新
                    RefreshState.Refreshing -> {
                        smartRefreshLayout?.finishRefresh()
                        controller?.setData(data)
                    }
                    // 下拉刷新
                    RefreshState.Loading -> {
                        if (currentContentSize == data?.contents?.size) {
                            smartRefreshLayout?.finishLoadMoreWithNoMoreData()
                        } else {
                            smartRefreshLayout?.finishLoadMore()
                            controller?.setData(data)
                        }
                    }
                    else -> {
                        smartRefreshLayout?.finishRefresh()
                        smartRefreshLayout?.finishLoadMore()
                        when (vm.smartRefreshState) {
                            LoadState.REFRESHING, LoadState.FAIL, LoadState.UPDATE_ITEM -> {
                                controller?.setData(data)
                            }
                        }
                    }
                }

                if (data?.contents?.size == 0) {
                    smartRefreshLayout?.setEnableLoadMore(false)
                } else {
                    smartRefreshLayout?.setEnableLoadMore(needLoadMore)
                }

                vm.smartRefreshState = LoadState.NONE
            })
        }

        // Epoxy Controller
        controller?.apply {
            // 空介面按鈕處理
            onEmptyButtonClick = View.OnClickListener {
                onClickItemEmptyButton()
            }
            // 無網絡介面按鈕處理
            onNoNetWorkButtonClick = View.OnClickListener {
                onClickItemNetErrorButton()
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(100)
                    smartRefreshLayout?.autoRefresh()
                }
            }
            // 先放空數據
            setData(vm.state)
        }

        // SmartRefreshLayout
        smartRefreshLayout?.apply {
            setEnableRefresh(needRefresh)
            if (needRefresh) setOnRefreshListener(this@BaseListFragment)
            setEnableLoadMore(needLoadMore)
            if (needLoadMore) setOnLoadMoreListener(this@BaseListFragment)

            setEnableOverScrollDrag(true)
            setEnableFooterFollowWhenNoMoreData(true)

            if (!needLoadMore && !needRefresh) setEnablePureScrollMode(true)

            onLoadData(true)
        }

        initView()
        initListener()
    }

    override fun bindAnnotationValue() {

        val anno = this::class.java.declaredAnnotations
            .filterIsInstance<BindFragment>()
            .firstOrNull()
            ?.let { annotation ->
                if (annotation.layout != 0)
                    layout = annotation.layout

                if (!annotation.hasRefresh) {
                    needRefresh = annotation.hasRefresh
                }

                if (!annotation.hasLoadMore) {
                    needLoadMore = annotation.hasLoadMore
                }

                if (annotation.hasTransparentToolBar) {
                    hasTransToolbar = annotation.hasTransparentToolBar
                }

                if (annotation.hasLazyLoading) {
                    hasLazyLoading = true
                }

                if (annotation.hasCoord) {
                    if (layout == 0) layout = R.layout.fragment_list_with_coord_base
                }

                if (annotation.hasTransparentToolBar) {
                    if (layout == 0) layout = R.layout.fragment_list_base_with_trans_bar
                }

                isNeedEventBus = annotation.hasEventBus == true
            }

        if (anno == null || layout == 0)
            layout = R.layout.fragment_list_base
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        smartRefreshLayout?.resetNoMoreData()
        vm.smartRefreshState = LoadState.REFRESHING
        onLoadData(true)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        currentContentSize = vm.state.contents.size
        vm.smartRefreshState = LoadState.LOADING
        onLoadData(false)
    }

    fun getColorWithAlpha(color: Int, ratio: Float): Int {
        var newColor = 0
        val alpha = Math.round(Color.alpha(color) * ratio)
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        newColor = Color.argb(alpha, r, g, b)
        return newColor
    }
}