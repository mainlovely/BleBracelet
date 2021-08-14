package com.zhj.bluetooth.sdkdemo.utils.util

import java.text.ParseException
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author seven
 * @createTime 2019/5/28
 * @description 时间工具类
 */
object DateUtils {

    // --------------------------------------- 获取当前时间 ----------------------------------------------

    // 获取当前时间 yyyy-MM-dd HH:mm:ss
    fun getNow(): String {
        return try {
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            val today = Calendar.getInstance().time
            df.format(today)
        } catch (exception: Exception) {
            ""
        }
    }

    // 获取当前时间 yyyy-MM-dd
    fun getNowDate(): String {
        return try {
            val df = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            df.format(Date())
        } catch (exception: Exception) {
            ""
        }
    }

    // ---------------------------------------- 时间转换 -----------------------------------------------

    // string - yyyy-MM-dd
    fun strToDate(strDate: String?): Date {
        strDate?.let {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val pos = ParsePosition(0)
            return formatter.parse(strDate, pos)
        }
        return Date()
    }

    // date - MM-dd
    fun dateToOnlyDateStr15(date: Date): String {
        val simpleDateFormat = SimpleDateFormat("MM/dd")
        return simpleDateFormat.format(date)
    }


    // string - yyyy-MM-dd HH:mm:ss
    fun dateStrToDate(string: String): Date {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return simpleDateFormat.parse(string)
    }

    // string - yyyy-MM-dd HH:mm
    fun dateStrToDateStr(string: String): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return simpleDateFormat.format(simpleDateFormat.parse(string))
    }

    // string - yyyy/MM/dd HH:mm:ss
    fun dateStrToDateStr2(string: String): String {
        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        return simpleDateFormat.format(simpleDateFormat.parse(string))
    }

    fun dateStrToPointDateStr(string: String): String {
        string?.let {
            val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
            return format.format(dateStrToDate(string))
        }
        return ""
    }

    // date - yyyy-MM-dd
    fun dateToOnlyDateStr(date: Date): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        return simpleDateFormat.format(date)
    }

    // date - yyyy-MM-dd HH:mm
    fun dateToFormatDate(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return format.format(date)
    }



    // date - yyyy年MM月dd日
    fun dateToChineseDateStr(date: Date): String {
        val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm")
        return simpleDateFormat.format(date)
    }

    // date - yyyy年MM月dd日 HH:mm
    fun dateToChineseTimeStr(date: Date): String {
        val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm")
        return simpleDateFormat.format(date)
    }

    // time - yyyy-MM-dd HH:mm:ss
    fun currentTimeToFormatDate(time: Long): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return format.format(Date(time))
    }

    fun currentTimeToFormatDate(): String {
        val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        return format.format(System.currentTimeMillis())
    }

    // time - yyyy-MM-dd HH:mm
    fun currentHourTimeToFormatDate(time: Long): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return format.format(Date(time))
    }

    // time - yyyy-MM-dd
    fun currentOnlyFormatDate(time: Long): String {
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(Date(time))
    }

    fun getYear(date: Date?): String {
        return if (date == null) "" else SimpleDateFormat("yy", Locale.getDefault()).format(date)
    }

    fun getAll(date: Date?): String {
        return if (date == null) "" else SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(date)
    }
    fun getMonth(date: Date?): String {
        return if (date == null) "" else SimpleDateFormat("MM", Locale.getDefault()).format(date)
    }

    fun getDay(date: Date?): String {
        return if (date == null) "" else SimpleDateFormat("dd", Locale.getDefault()).format(date)
    }

    fun getTimeWithoutS(date: Date?): String {
        return if (date == null) "" else SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
    }

    /**
     * 获取一天开始
     */
    fun getStartTime(time: Long?): String {
        val start = Calendar.getInstance()
        start.timeInMillis = time!!
        start.set(Calendar.HOUR_OF_DAY, 0)
        start.set(Calendar.MINUTE, 0)
        start.set(Calendar.SECOND, 0)
        start.set(Calendar.MILLISECOND, 0)
        return dateToFormatDate(start.time)
    }

    /**
     * 获取一天结束
     */
    fun getEndTime(time: Long?): String {
        val end = Calendar.getInstance()
        end.timeInMillis = time!!
        end.set(Calendar.HOUR_OF_DAY, 23)
        end.set(Calendar.MINUTE, 59)
        end.set(Calendar.SECOND, 59)
        end.set(Calendar.MILLISECOND, 59)
        return dateToFormatDate(end.time)
    }

    /**
     * 如果date1 > date2 返回为true 即开始时间大于结束时间
     */
    fun compareDateByGetTime(date1: Date, date2: Date): Boolean {
        return when {
            date1.time < date2.time -> false
            date1.time > date2.time -> true
            else -> false
        }
    }


    private fun formatTwoDigit(num: Int): String {
        return when (num < 10) {
            true -> "0$num"
            else -> "" + num
        }
    }

    private fun toSeconds(date: Long): Long {
        return date / 1000L
    }

    private fun toMinutes(date: Long): Long {
        return toSeconds(date) / 60L
    }

    private fun toHours(date: Long): Long {
        return toMinutes(date) / 60L
    }

    private fun toDays(date: Long): Long {
        return toHours(date) / 24L
    }

    private fun toMonths(date: Long): Long {
        return toDays(date) / 30L
    }

    private fun toYears(date: Long): Long {
        return toMonths(date) / 365L
    }




    /**
     * 秒转换为指定格式的日期
     * @param second
     * @param patten
     * @return
     */
    fun secondToHour(time: Long): String {
        val totalSeconds = (time / 1000).toInt()
        val seconds = totalSeconds % 3600 % 60
        val minutes = totalSeconds % 3600 / 60
        val hours = totalSeconds / 3600
        return String.format(
            "%02d:%02d:%02d",
            hours,
            minutes,
            seconds
        )
    }


    /**
     * 判断是否超过今天
     */
    fun compareIsMoreThanToday(time: String): Boolean {
        val nowTime = Date().time
        val theTime = SimpleDateFormat("yyyy-MM-dd").parse(time).time
        if (nowTime - theTime < 0) {
            return true
        }
        return false

    }



    fun getWeekOfToday(time: String?): Int {
        time?.let {
            val format = SimpleDateFormat("yyyy-MM-dd")
            val c = Calendar.getInstance()
            try {
                c.time = format.parse(time)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return c.get(Calendar.DAY_OF_WEEK)
        }
        return 0
    }

    /**
     * 时间显示
     * @param time 毫秒
     * @return
     */
    fun formatLongToVideoTime(time: Long?): String {
        if (time == null) {
            return "00:00"
        }
        val millisSeconds = (time % 1000).toInt()
        val totalSeconds = (time / 1000).toInt()
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600

        return if (hours > 0)
            String.format("%02d:%02d:%02d", hours, minutes, seconds) // 显示时分秒
        else
            (if (minutes > 0)
                String.format("%02d:%02d", minutes, seconds)      // 显示分钟和秒
            else
                if (totalSeconds > 0)
                    String.format("%02d:%02d", 0, totalSeconds) // 显示秒和毫秒
                else
                    String.format("%02d:%02d", 0, 0))   // 只显示毫秒
    }


    val fullTimeNoSplit: String
        get() = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())




    fun isSameDay(time1: Long, time2: Long): Boolean {
        try {
            val nowCal = Calendar.getInstance();
            val dataCal = Calendar.getInstance();
            val df1 = SimpleDateFormat("yyyy-MM-dd  HH:mm:ss")
            val df2 = SimpleDateFormat("yyyy-MM-dd  HH:mm:ss")
            val data1 = df1.format(time1)
            val data2 = df2.format(time2)
            val now = df1.parse(data1)
            val date = df2.parse(data2)
            nowCal.time = now
            dataCal.time = date
            return isSameDay(nowCal, dataCal)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun isSameDay(date1: Calendar?, date2: Calendar?): Boolean {
        return if(date1 != null && date2 != null) {
            (date1.get(Calendar.ERA) == date2.get(Calendar.ERA)
                    && date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)
                    && date1.get(Calendar.DAY_OF_YEAR) == date2.get(Calendar.DAY_OF_YEAR));
        } else {
            false;
        }
    }

}