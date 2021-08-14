package com.zhj.bluetooth.sdkdemo

import android.app.Application
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatDelegate
import com.airbnb.epoxy.EpoxyController
import com.zhj.bluetooth.sdkdemo.di.allApp
import com.zhj.bluetooth.sdkdemo.utils.util.ResourceProvider
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

val res: ResourceProvider by lazy { App.res }
class App: Application(), ApplicationLifecycle {
    companion object {
        lateinit var instance: App
        lateinit var res: ResourceProvider
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        //關閉dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(allApp)
        }

        initRes()
        initEpoxyDefaultSetting()

    }

    private fun initRes() {
        res = ResourceProvider.getInstance(this.applicationContext)
    }

    /**
     * Model building and Diffing. By default these use the main thread,
     * but they can be changed to allow async work for performance improvement.
     */
    private fun initEpoxyDefaultSetting() {
        val handlerThread = HandlerThread("epoxy")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        EpoxyController.defaultDiffingHandler = handler
        EpoxyController.defaultModelBuildingHandler = handler
    }
}