package com.zhj.bluetooth.sdkdemo.ui.base

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gyf.immersionbar.ktx.immersionBar
import com.gyf.immersionbar.ktx.showStatusBar
import org.greenrobot.eventbus.EventBus
import com.zhj.bluetooth.sdkdemo.utils.annotation.BindFragment
import com.zhj.bluetooth.sdkdemo.utils.extension.log
import com.zhj.bluetooth.sdkdemo.ui.base.contract.IBaseFragment
import com.zhj.bluetooth.sdkdemo.view.dialog.LoadingDialog
import com.zhj.bluetooth.sdkdemo.BR
import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.data.model.base.HideLoadingState
import com.zhj.bluetooth.sdkdemo.data.model.base.ShowLoadingState
import com.zhj.bluetooth.sdkdemo.utils.extension.color
import com.zhj.bluetooth.sdkdemo.view.CJMAppBar

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel<*>> : Fragment(),
    IBaseFragment<V> {

    companion object {
        const val NO_BINDING_VARIABLE = -999
        const val TAG = "BaseFragment"
    }

    open val tagClass = TAG
    val vm: V by createViewModel()
    lateinit var binding: T

    private var mProgressDialog: LoadingDialog? = null
    protected var layout: Int = 0
    private var bindingVariable = BR.vm
    protected var isNeedEventBus = false
    protected var hasLazyLoading = false

    abstract override fun createViewModel(): Lazy<V>
    abstract override fun observeViewModel(vm: V)

    protected val appBar: CJMAppBar? by lazy { binding.root.findViewById<CJMAppBar>(R.id.toolbar) }
    protected val statusBar: View? by lazy { binding.root.findViewById<View>(R.id.status_bar_view) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindAnnotationValue()

        if (isNeedEventBus) {
            EventBus.getDefault().unregister(this)
            EventBus.getDefault().register(this)
        }

        binding = DataBindingUtil.inflate(inflater, layout, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view, savedInstanceState)
    }

    private fun init(view: View, savedInstanceState: Bundle?) {
        log("$TAG ${this.javaClass.name}")

        binding.root.setBackgroundColor(R.color.color_1D1D1D.color())

        when {
            bindingVariable != NO_BINDING_VARIABLE -> {
                binding.setVariable(bindingVariable, vm)
                binding.lifecycleOwner = this
            }
        }

        if (appBar != null) {
            setAppBarInfo(appBar!!)
        }

        appBar?.onClickBackLeftIcon(View.OnClickListener {
            activity?.onBackPressed()
        })

        if (statusBar != null) {
            immersionBar {
                transparentStatusBar()
                statusBarView(statusBar)
                showStatusBar()
            }
        } else {
            immersionBar {
                showStatusBar()
            }
        }

        // 重要不能刪走!! 不然子fragment有機會能點到父fragment的view層
        binding.root.apply {
            isClickable = true
            isFocusable = true
        }

        observeViewModel(vm)

        if (!vm.loading.hasObservers()) {
            vm.loading.observe(activity!!, Observer {
                it?.let { state ->
                    when (state) {
                        is ShowLoadingState -> {
                            showLoading()
                        }
                        is HideLoadingState -> {
                            hideLoading()
                        }
                    }
                }
            })
        }

        if(tagClass == TAG) {
            initData()
            initView()
            initListener()
        }

        isAttaching = true
    }

    override fun bindAnnotationValue() {
        val annotation = this::class.java.declaredAnnotations
            .filterIsInstance<BindFragment>()
            .firstOrNull()
            ?.let { annotation ->
                if (layout == 0 && annotation.layout == 0) {
                    throw Exception("Please bind the R.layout.xxx resource id with @BindFragment annotation.")
                } else {
                    if (layout == 0)
                        layout = annotation.layout
                }

                isNeedEventBus = annotation.hasEventBus == true

                if (annotation.bindingVariable != NO_BINDING_VARIABLE) {
                    bindingVariable = annotation.bindingVariable
                }

                if (annotation.hasLazyLoading) {
                    hasLazyLoading = true
                }
            }

        if (annotation == null && layout == 0) {
            throw Exception("Please bind @BindFragment(...)")
        }
    }

    override fun initView() {}
    override fun initListener() {}
    override fun initData() {}
    override fun setAppBarInfo(appBar: CJMAppBar) {}

    override fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog?.isVisible == true) {
            mProgressDialog?.dismiss()
        }
    }

    override fun showLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = LoadingDialog()
        }
        if (mProgressDialog?.isVisible == true) {
            return
        }
        if (fragmentManager != null)
            mProgressDialog?.show(parentFragmentManager, "")
    }

    var isAttaching = false

    override fun youCanSeeMe() {
        log("$TAG youCanSeeMe ${this.javaClass.name}")
    }

    override fun youCannotSeeMe() {
        log("$TAG youCannotSeeMe ${this.javaClass.name}")
    }

    override fun onDestroy() {
        super.onDestroy()

        log("$TAG onDestroy")

        if (isNeedEventBus) {
            EventBus.getDefault().unregister(this)
        }

        appBar?.apply {
            onClickBackLeftIcon(null)
            onClickBackRightIcon(null)
            onClickMainRightIcon(null)
            onClickMainRightIcon2(null)
        }
        hideLoading()

        vm.navigator = null
        vm.childNavigator = null
        mProgressDialog = null
        isAttaching = false
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        var animation = super.onCreateAnimation(transit, enter, nextAnim)

        // HW layer support only exists on API 11+
        if (Build.VERSION.SDK_INT >= 19) {
            if (animation == null && nextAnim != 0) {
                animation = AnimationUtils.loadAnimation(activity, nextAnim)
            }

            if (animation != null) {
                view?.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                animation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(p0: Animation?) {
                        // do nothing
                    }

                    override fun onAnimationStart(p0: Animation?) {
                        // do nothing
                    }

                    override fun onAnimationEnd(p0: Animation?) {
                        getView()?.setLayerType(View.LAYER_TYPE_NONE, null)
                    }

                })
            }
        }

        return animation
    }

}