package com.zhj.bluetooth.sdkdemo.ui.base

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.gyf.immersionbar.ktx.immersionBar
import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.data.model.base.HideLoadingState
import com.zhj.bluetooth.sdkdemo.data.model.base.ShowLoadingState
import com.zhj.bluetooth.sdkdemo.data.model.base.State

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>>
    : AppCompatActivity() {

    val vm: V by createViewModel()
    lateinit var binding: T

    private var initialLocale: String? = null
    private var mProgressDialog: Dialog? = null

    var isActivityClickable: Boolean = true

    abstract fun getLayoutId(): Int
    abstract fun getBindingVariable(): Int
    abstract fun createViewModel(): Lazy<V>
    abstract fun observeViewModel(vm: V)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, getLayoutId())
        when {
            getBindingVariable() != NO_BINDING_VARIABLE -> {
                binding.setVariable(getBindingVariable(), vm)
                binding.lifecycleOwner = this
            }
        }
        observeViewModel(vm)
        immersionBar {
            transparentStatusBar()
            statusBarDarkFont(false, 1.0f)
            navigationBarColor(R.color.transparent)
        }
    }

    protected inline fun LiveData<State>.observeStates(
        owner: LifecycleOwner,
        crossinline observer: (t: State) -> Unit
    ) {
        this.observe(owner, Observer {
            it?.let { state ->
                when (state) {
                    is ShowLoadingState -> {
                        showLoading()
                        return@Observer
                    }
                    is HideLoadingState -> {
                        hideLoading()
                        return@Observer
                    }
                }
                observer(state)
            }
        })
    }

    override fun onDestroy() {
        hideLoading()
        super.onDestroy()
    }


    fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing && !this.isDestroyed) {
            mProgressDialog?.dismiss()
        }
    }

    fun showLoading() {
        if (!this.isDestroyed) {
            hideLoading()
        }
    }

    protected fun toast(str: String) {
//        ToastUtils.showShort(str)
    }

    companion object {
        const val NO_BINDING_VARIABLE = -999
    }
}