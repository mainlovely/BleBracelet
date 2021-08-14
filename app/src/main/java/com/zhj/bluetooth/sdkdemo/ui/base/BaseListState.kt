package com.zhj.bluetooth.sdkdemo.ui.base

import com.zhj.bluetooth.sdkdemo.ui.base.epoxy.itemview.ItemEmptyViewFactory


abstract class BaseListState<T> {
    abstract var contents: List<T>
    open var itemEmptyViewData = ItemEmptyViewFactory.create(ItemEmptyViewFactory.NO_SEARCH_RESULT)
}