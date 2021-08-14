package com.zhj.bluetooth.sdkdemo.ui.base

import androidx.lifecycle.MutableLiveData
import com.zhj.bluetooth.sdkdemo.data.model.constant.LoadState

abstract class BaseListViewModel<S: BaseListState<*>, N> : BaseViewModel<N>() {
    override var TAG = "BaseListViewModel"
    abstract val listState: S

    var pageSize = 0
    val items: MutableLiveData<S> by lazy { MutableLiveData<S>().apply { value = listState }  }
    val state
        get() = items.value!!

    var smartRefreshState = LoadState.NONE

    open fun onFetchData(loadingState: Int) {}

    override fun onCleared() {
        super.onCleared()
        navigator = null
    }

    @Synchronized
    fun setState(loadState: Int, isReverse: Boolean = false, block: () -> S? = {null}) {
        smartRefreshState = loadState
        items.value = when(smartRefreshState) {
            LoadState.REFRESHING -> {
                if(isReverse)
                    state.apply { contents = (contents + block.invoke()?.contents.orEmpty()).distinct() as List<Nothing> }
                else
                    block.invoke()?.apply { contents = contents.distinct() as List<Nothing> }
            }
            LoadState.LOADING -> {
                if(isReverse)
                    block.invoke()?.apply { contents = contents.distinct() as List<Nothing> }
                else
                    state.apply { contents = (contents + block.invoke()?.contents.orEmpty()).distinct() as List<Nothing> }
            }
            LoadState.UPDATE_ITEM -> {
                block.invoke()?.apply { contents = contents.distinct() as List<Nothing> }
            }
            else -> {
                // 如果有什麼fail情況 撤回先前 getPageNo() 的 pageSize
                if(pageSize > 1) {
                    pageSize--
                }
                items.value
            }
        }
    }

    open fun getPageNo(isReverse: Boolean = false): Int {
        if (smartRefreshState == LoadState.REFRESHING) {
            if(isReverse)
                pageSize++
            else
                pageSize = 1
        } else {
            if(isReverse)
                pageSize = 1
            else
                pageSize++
        }
        return pageSize
    }



    open fun setEmptyViewState() {

    }
}