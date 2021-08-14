package com.zhj.bluetooth.sdkdemo.ui.base.dialog

import android.os.Bundle
import android.view.View
import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.databinding.DialogAlertConfirmBaseBinding
import com.zhj.bluetooth.sdkdemo.ui.base.BaseFragment
import com.zhj.bluetooth.sdkdemo.utils.annotation.BindFragment
import com.zhj.bluetooth.sdkdemo.utils.extension.color
import com.zhj.bluetooth.sdkdemo.utils.extension.selfDestroy
import com.zhj.bluetooth.sdkdemo.utils.extension.setSafeOnClickListener
import com.zhj.bluetooth.sdkdemo.utils.util.KeyboardUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

@BindFragment(layout = R.layout.dialog_alert_confirm_base)
class AlertConfirmDialogFragment constructor(
    private val title: String,
    private var listener: ConfirmDialogListener?,
    private val clickOut: Boolean = true
) : BaseFragment<DialogAlertConfirmBaseBinding, DialogVM>() {

    override fun createViewModel() = viewModel<DialogVM>()

    override fun observeViewModel(vm: DialogVM) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.setBackgroundColor(R.color.transparent.color())

        // 重要不能刪走!! 不然子fragment有機會能點到父fragment的view層

        binding.apply {
            root.isClickable = true
            root.isFocusable = true

            mask.apply {
                setSafeOnClickListener {
                    if(clickOut){
                        selfDestroy()
                    }
                }
            }

            tvTitle.apply {
                text = title
            }

            tvYes.apply {
                setSafeOnClickListener {
                    listener?.onClickYes()
                    selfDestroy()
                }

            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        KeyboardUtils.hideSoftInput(activity!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
    }

    interface ConfirmDialogListener {
        fun onClickYes()
    }
}