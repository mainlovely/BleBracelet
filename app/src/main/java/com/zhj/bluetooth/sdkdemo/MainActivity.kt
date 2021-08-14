package com.zhj.bluetooth.sdkdemo

import android.content.Intent
import android.os.Bundle
import com.zhj.bluetooth.sdkdemo.ui.base.BaseActivity
import com.zhj.bluetooth.sdkdemo.ui.base.contract.SelfBackable
import com.zhj.bluetooth.sdkdemo.ui.base.navigation.FragmentStack
import com.zhj.bluetooth.sdkdemo.utils.extension.log
import com.zhj.bluetooth.sdkdemo.utils.extension.navigateTo
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.zhj.bluetooth.sdkdemo.databinding.ActivityMainBinding
import com.zhj.bluetooth.sdkdemo.utils.util.ScreenUtils

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    private var exitTime: Long = 0//离开时间

    override fun getLayoutId() = R.layout.activity_main
    override fun getBindingVariable() = BR.vm
    override fun createViewModel() = viewModel<MainViewModel>()

    override fun observeViewModel(vm: MainViewModel) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FragmentStack.mainActivity = this
        navigateTo(MainFragment(), needAnimation = false)
    }

    override fun onBackPressed() {
        log("****** FragmentStack.getFragmentsSize() ${FragmentStack.getFragmentsSize()}")

        /**
         * 交由 fragment 自己控制上一頁, [MainActivity] 不插手控制
         * 可以參考
         **/
        when (val topFragment = FragmentStack.getTopFragment()) {
            is SelfBackable -> {
                topFragment.onBackPressed()
                return
            }
        }

        if (ScreenUtils.isLandscape()) {
            com.zhj.bluetooth.sdkdemo.utils.util.ScreenUtils.setPortrait(this)
        }

        if (FragmentStack.getFragmentsSize() > 1)
            FragmentStack.closeFragment(FragmentStack.getTopFragment())
        else {
            if (System.currentTimeMillis() - exitTime > 2000) {
                com.zhj.bluetooth.sdkdemo.utils.util.ToastUtils.showLong(R.string.quit_app)
                exitTime = System.currentTimeMillis()
            } else {
                moveTaskToBack(true)
//                android.os.Process.killProcess(android.os.Process.myPid())
//                System.exit(1)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        /**
         * 如用戶離開APP再重新進入頁面, 用於知道用戶在那個頁面開啟
         */
        FragmentStack.youCanSeeMe()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        FragmentStack.getTopFragment()?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

}