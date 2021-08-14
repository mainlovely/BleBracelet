package com.zhj.bluetooth.sdkdemo.view.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.zhj.bluetooth.sdkdemo.R
import com.zhj.bluetooth.sdkdemo.databinding.LayoutLoadingBinding
import com.zhj.bluetooth.sdkdemo.utils.util.ColorUtils

class LoadingDialog : DialogFragment() {
    var binding: LayoutLoadingBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_loading, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        super.onActivityCreated(savedInstanceState)

        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(ColorUtils.getColor(R.color.color_20_black)))
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        }
    }
}