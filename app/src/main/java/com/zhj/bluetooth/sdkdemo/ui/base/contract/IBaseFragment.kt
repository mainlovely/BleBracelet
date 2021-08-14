package com.zhj.bluetooth.sdkdemo.ui.base.contract

import com.zhj.bluetooth.sdkdemo.ui.base.BaseViewModel
import com.zhj.bluetooth.sdkdemo.view.CJMAppBar


interface IBaseFragment<VM : BaseViewModel<*>> {
    /**
     * 用於 @BindFragment(layout = R.layout.xxxxxx)
     */
    fun bindAnnotationValue()

    /**
     * 用於初始化 ViewModel
     */
    fun createViewModel(): Lazy<VM>

    /**
     * 用於觀察 ViewModel navigator 的變化
     */
    fun observeViewModel(vm: VM)

    /**
     * 初始化處理, 不會添加任何邏輯在 BaseFragment
     * 可以基於情況使用
     */
    fun initView()
    fun initListener()
    fun initData()

    /**
     * youCanSeeMe 的执行频率就像名字一样  只要“你肉眼看见”  就会执行一遍
     */
    fun youCanSeeMe()

    /**
     * youCannotSeeMe 的执行频率就像名字一样  只要“你肉眼看不见”  就会执行一遍
     */
    fun youCannotSeeMe()

    /**
     * 初始化數據前, 先設置好 CJMAppBar
     */
    fun setAppBarInfo(appBar: CJMAppBar)

    /**
     * 用於控制進度條
     */
    fun showLoading()
    fun hideLoading()
}