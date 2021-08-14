package com.zhj.bluetooth.sdkdemo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.zhj.bluetooth.sdkdemo.data.model.HealthSleepItemCopy
import com.zhj.bluetooth.sdkdemo.data.model.HeartHistoryIsUploadSuccessData
import com.zhj.bluetooth.sdkdemo.data.model.SleepHistoryIsUploadSuccessData
import com.zhj.bluetooth.sdkdemo.data.model.SportHistoryIsUploadSuccessData
import com.zhj.bluetooth.sdkdemo.data.model.base.BaseResponse
import com.zhj.bluetooth.sdkdemo.data.model.constant.LoadState
import com.zhj.bluetooth.sdkdemo.data.model.constant.RespCode
import com.zhj.bluetooth.sdkdemo.data.source.local.Prefs
import com.zhj.bluetooth.sdkdemo.data.source.repository.UploadBleRxRepository
import com.zhj.bluetooth.sdkdemo.data.source.repository.UploadBleRxRepositoryImpl
import com.zhj.bluetooth.sdkdemo.ui.base.BaseNavigator
import com.zhj.bluetooth.sdkdemo.ui.base.BaseViewModel
import com.zhj.bluetooth.sdkdemo.utils.extension.launch
import com.zhj.bluetooth.sdkdemo.utils.extension.string
import com.zhj.bluetooth.sdkdemo.utils.util.DateUtils
import com.zhj.bluetooth.sdkdemo.utils.util.GsonUtils
import com.zhj.bluetooth.sdkdemo.utils.util.ToastUtils
import com.zhj.bluetooth.zhjbluetoothsdk.bean.*
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleCallback
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.*


interface MainNavigator : BaseNavigator {
    fun toDeviceStatus()
    fun showDevicePower(power: Int)
    fun showDeviceGoalStep(curStep: Int, maxStep: Int)
    fun finishRefresh()
    fun toHeartRatePage()
    fun toSleepPage()
    fun toSportPage()
    fun toAlarmClock()
    fun toSedentaryReminder()
    fun toSwitchPage()

}

class MainViewModel(private val uploadBleRxRepository: UploadBleRxRepository) : BaseViewModel<MainNavigator>() {
    val deviceStateTv = MutableLiveData<String>()

    /** 设备信息 **/
    val deviceInfoShow = MutableLiveData<Boolean>()
    val deviceProductTv = MutableLiveData<String>()
    val deviceAddressTv = MutableLiveData<String>()
    val devicePowerTv = MutableLiveData<String>()
    val deviceTotalCalory = MutableLiveData<String>()
    val deviceTotalDistance = MutableLiveData<String>()
    val deviceHeartRate = MutableLiveData<String>()
    val deviceSleepTime = MutableLiveData<String>()


    var connectedBleDevice: BLEDevice? = null
    var bleCurStep: Int = 0
    var isRefreshing = false

    private val calendarHeart: Calendar by lazy { Calendar.getInstance() }
    private val calendarSleep: Calendar by lazy { Calendar.getInstance() }
    private val calendarSport: Calendar by lazy { Calendar.getInstance() }

    fun init() {
        deviceStateTv.value = R.string.main_no_pairing.string()
        deviceInfoShow.value = false
        deviceProductTv.value = R.string.device_name.string()
        deviceAddressTv.value = R.string.main_mac_address.string()
        devicePowerTv.value = R.string.device_power.string()
        deviceTotalCalory.value = res.getString(R.string.device_calory, "0")
        deviceTotalDistance.value = res.getString(R.string.device_distance, "0")
        deviceHeartRate.value = res.getString(R.string.heart_rate_of_minute, "0")
        deviceSleepTime.value = "0小时0分"
    }

    fun toDeviceStatus() {
        navigator?.toDeviceStatus()
    }

    fun getDeviceData() {
        launch {
//            getBleInfo()
            getBlePower()
            getBleCurStepAndHeart()
            getBleGoal()
            getCurrentRate()
            if (isRefreshing) {
                navigator?.finishRefresh()
            }
        }
    }


    /**
     * 设备信息
     */
    private fun getBleInfo() {
        BleSdkWrapper.getDeviceInfo(bleCallBack)
    }

    /**
     * 电量
     */
    private fun getBlePower() {
        BleSdkWrapper.getPower(bleCallBack)
    }

    /**
     * 运动数据(步数 + 心率)
     */
    private fun getBleCurStepAndHeart() {
        BleSdkWrapper.getCurrentStep(bleCallBack)
    }

    /**
     * 目标信息
     */
    private fun getBleGoal() {
        BleSdkWrapper.getTarget(bleCallBack)
    }

    /**
     * 获取当前心率
     */
    private fun getCurrentRate() {
        BleSdkWrapper.getTarget(bleCallBack)
    }

    private val bleCallBack = object : BleCallback {
        override fun complete(p0: Int, p1: Any?) {
            val result = p1 as HandlerBleDataResult
            when (result.data) {
                is BLEDevice -> {//设备信息
                    if (result.data is BLEDevice) {
                        val device = (result.data as BLEDevice)
                        Log.e("seven", "设备信息:  device = $device")
                        deviceProductTv.value =
                            R.string.device_name.string() + device.mDeviceProduct
                        deviceAddressTv.value =
                            R.string.main_mac_address.string() + device.mDeviceAddress
                    }
                }
                is Int -> {//电量
                    if (result.data is Int) {
                        val power = result.data as Int
                        Log.e("seven", "电量:  power = $power")
                        devicePowerTv.value = "${R.string.device_power.string()}$power%"
                        navigator?.showDevicePower(power)
                    }
                }
                is HealthSport -> {// 运动步数
                    if(result.data is HealthSport){
                        val sport = result.data as HealthSport
                        Log.e("seven", "运动数据:  sport = $sport")
                        bleCurStep = sport.totalStepCount
                        deviceTotalCalory.value =
                            res.getString(R.string.device_calory, sport.totalCalory.toString())
                        deviceTotalDistance.value =
                            res.getString(R.string.device_distance, sport.totalDistance.toString())

                    }
                }
                is Goal -> {// 目标数据
                    if(result.data is Goal){
                        val goal = result.data as Goal
                        Log.e("seven", "目标数据:  goal = $goal")
                        navigator?.showDeviceGoalStep(bleCurStep, goal.goalStep)
                    }
                }
                is HealthHeartRate -> {// 心率
                    if(result.data is HealthHeartRate){
                        val heartRate = result.data as HealthHeartRate
                        Log.e("seven", "当前心率:  heartRate = $heartRate")
                        deviceHeartRate.value = res.getString(
                            R.string.heart_rate_of_minute,
                            heartRate.silentHeart.toString()
                        )
                    }
                }
            }
        }

        override fun setSuccess() {

        }
    }

    fun toHeartRatePage() {
        navigator?.toHeartRatePage()
    }

    fun toSleepPage() {
        navigator?.toSleepPage()
    }

    fun toSportPage() {
        navigator?.toSportPage()
    }

    fun toAlarmClock() {
        navigator?.toAlarmClock()
    }

    fun toSedentaryReminder() {
        navigator?.toSedentaryReminder()
    }

    fun toSwitchPage(){
        navigator?.toSwitchPage()
    }



    /************************  设备历史数据上报   ***************************/
    fun checkUploadHistoryData(){
        if(connectedBleDevice == null || connectedBleDevice?.mDeviceName.isNullOrEmpty()){
            return
        }
        val curDate = DateUtils.getNowDate()

        // 历史心率
        val uploadHeartData = Prefs.heartUpload
        if(uploadHeartData?.uploadDate.isNullOrEmpty() || uploadHeartData?.uploadDate != curDate || !uploadHeartData.isUpload){
            getHistoryHeartRateData()
        }

        // 历史睡眠
        val uploadSleepData = Prefs.sleepUpload
        if(uploadSleepData?.uploadDate.isNullOrEmpty() || uploadSleepData?.uploadDate != curDate || !uploadSleepData.isUpload){
            getHistorySleepData()
            return
        }

        // 历史运动
        val uploadSportData = Prefs.sportUpload
        if(uploadSportData?.uploadDate.isNullOrEmpty() || uploadSportData?.uploadDate != curDate || !uploadSportData.isUpload){
            getHistorySportData()
            return
        }
    }


    /***********************************  历史心率  **************************************/
    private val allHeartRateList = mutableListOf<HealthHeartRateItem>()
    /**
     * 获取历史历史心跳数据
     */
    private fun getHistoryHeartRateData(){
        val year = calendarHeart[Calendar.YEAR]
        val month = calendarHeart[Calendar.MONTH] + 1
        val day = calendarHeart[Calendar.DATE]
        BleSdkWrapper.getHistoryHeartRateData(year, month, day, object : BleCallback {
            override fun complete(resultCode: Int, data: Any) {
                if (data is HandlerBleDataResult) {
                    val result = data as HandlerBleDataResult
                    if (result.data is List<*>) {
                        if (result.isComplete) {
                            if (result.hasNext) { //是否还有更多的历史数据
                                val healthHeartRateItems = result.data as List<HealthHeartRateItem>
                                allHeartRateList.addAll(healthHeartRateItems)
                                Log.e("seven", "历史心率数据： healthHeartRateItems.size = ${healthHeartRateItems.size}...allHeartRateList.size = ${allHeartRateList.size}...healthHeartRateItems = $healthHeartRateItems")
                                // 上传到后台
                                toUploadHeartHisToryData(healthHeartRateItems)
                                calendarHeart.add(Calendar.DATE, -1)
                                getHistoryHeartRateData()
                            }else{
                                Log.e("seven", "历史心率数据 222222")
                            }
                        }
                    }

                }
            }

            override fun setSuccess() {}
        })
    }


    private fun toUploadHeartHisToryData(items: List<HealthHeartRateItem>){
        launch {
            uploadBleRxRepository.braceletHeartbeat(connectedBleDevice?.mDeviceName.orEmpty(), GsonUtils.toJson(items),
                object : Observer<BaseResponse<Any>> {
                override fun onComplete() {
//                    Log.e("seven", "toUploadHeartHisToryData() onComplete()")
                }

                override fun onSubscribe(p0: Disposable) {
//                    Log.e("seven", "toUploadHeartHisToryData() onSubscribe()")
                }

                override fun onNext(p0: BaseResponse<Any>) {
                    Log.e("seven", "toUploadHeartHisToryData() onNext() p0 = $p0")
                    if(p0.code == RespCode.SUCCESS){
                        Prefs.heartUpload = HeartHistoryIsUploadSuccessData(true, DateUtils.getNowDate())
                    }
                }
                override fun onError(p0: Throwable) {
                    Log.e("seven", "toUploadHeartHisToryData() onError() p0 = $p0")
                }

            } )
        }
    }


    /***********************************  历史睡眠  **************************************/
    private val allHealthSleepList = mutableListOf<HealthSleepItemCopy>()
    private val allSportList = mutableListOf<HealthSportItem>()
    private fun getHistorySleepData(){
        val year = calendarSleep[Calendar.YEAR]
        val month = calendarSleep[Calendar.MONTH] + 1
        val day = calendarSleep[Calendar.DATE]
        BleSdkWrapper.getStepOrSleepHistory(year, month, day, object : BleCallback {
            override fun complete(resultCode: Int, data: Any) {
                if (data is HandlerBleDataResult) {
                    val result = data as HandlerBleDataResult
                    if (result.data is List<*>) {
                        if (result.isComplete) {
                            if (result.hasNext) { //是否还有更多的历史数据
                                // 上传到历史睡眠到后台
                                if(!result.sleepItems.isNullOrEmpty()){
                                    val healthSleepItems = result.sleepItems as List<HealthSleepItem>
                                    val convertSleepList = mutableListOf<HealthSleepItemCopy>()
                                    healthSleepItems.forEach {
                                        convertSleepList.add(HealthSleepItemCopy().convertTo(it))
                                    }
                                    Log.e("seven", "11111历史睡眠数据： convertSleepList.size = ${convertSleepList.size}...allHealthSleepList.size = ${allHealthSleepList.size}...convertSleepList = $convertSleepList")
                                    allHealthSleepList.addAll(convertSleepList)
                                    toUploadSleepHisToryData(convertSleepList)
                                }
                                calendarSleep.add(Calendar.DATE, -1)
                                getHistorySleepData()
                            }else{
                                Log.e("seven", "历史睡眠 222222")
                            }
                        }
                    }

                }
            }

            override fun setSuccess() {}
        })
    }

    private fun toUploadSleepHisToryData(items: List<HealthSleepItemCopy>){
        launch {
            uploadBleRxRepository.braceletSleep(connectedBleDevice?.mDeviceName.orEmpty(), GsonUtils.toJson(items),
                object : Observer<BaseResponse<Any>> {
                    override fun onComplete() {
//                        Log.e("seven", "toUploadSleepHisToryData() onComplete()")
                    }

                    override fun onSubscribe(p0: Disposable) {
//                        Log.e("seven", "toUploadSleepHisToryData() onSubscribe()")
                    }

                    override fun onNext(p0: BaseResponse<Any>) {
                        Log.e("seven", "toUploadSleepHisToryData() onNext() p0 = $p0")
                        if(p0.code == RespCode.SUCCESS){
                            Prefs.sleepUpload = SleepHistoryIsUploadSuccessData(true, DateUtils.getNowDate())
                        }
                    }
                    override fun onError(p0: Throwable) {
                        Log.e("seven", "toUploadSleepHisToryData() onError() p0 = $p0")
                    }

                } )
        }
    }

    /***********************************  历史运动  **************************************/
    private fun getHistorySportData(){
        val year = calendarSport[Calendar.YEAR]
        val month = calendarSport[Calendar.MONTH] + 1
        val day = calendarSport[Calendar.DATE]
        BleSdkWrapper.getStepOrSleepHistory(year, month, day, object : BleCallback {
            override fun complete(resultCode: Int, data: Any) {
                if (data is HandlerBleDataResult) {
                    val result = data as HandlerBleDataResult
                    if (result.data is List<*>) {
                        if (result.isComplete) {
                            if (result.hasNext) { //是否还有更多的历史数据
                                // 上传到历史步数到后台
                                val sportItems = result.data as List<HealthSportItem>
                                allSportList.addAll(sportItems)
                                Log.e("seven", "11111历史步数数据： sportItems.size = ${sportItems.size}...allSportList.size = ${allSportList.size}...sportItems = $sportItems")
                                toUploadSportHisToryData(sportItems)
                                calendarSport.add(Calendar.DATE, -1)
                                getHistorySportData()
                            }else{
                                Log.e("seven", "历史步数 222222")
                            }
                        }
                    }

                }
            }

            override fun setSuccess() {}
        })
    }

    private fun toUploadSportHisToryData(items: List<HealthSportItem>){
        launch {
            uploadBleRxRepository.braceletMotion(connectedBleDevice?.mDeviceName.orEmpty(), GsonUtils.toJson(items),
                object : Observer<BaseResponse<Any>> {
                    override fun onComplete() {
//                        Log.e("seven", "toUploadSportHisToryData() onComplete()")
                    }

                    override fun onSubscribe(p0: Disposable) {
//                        Log.e("seven", "toUploadSportHisToryData() onSubscribe()")
                    }

                    override fun onNext(p0: BaseResponse<Any>) {
                        Log.e("seven", "toUploadSportHisToryData() onNext() p0 = $p0")
                        if(p0.code == RespCode.SUCCESS){
                            Prefs.sportUpload = SportHistoryIsUploadSuccessData(true, DateUtils.getNowDate())
                        }
                    }
                    override fun onError(p0: Throwable) {
                        Log.e("seven", "toUploadSportHisToryData() onError() p0 = $p0")
                    }

                } )
        }
    }

}