package com.zhj.bluetooth.sdkdemo.utils.extension

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import com.airbnb.epoxy.DataBindingEpoxyModel
import java.util.*

fun Int.string(): String {
    return com.zhj.bluetooth.sdkdemo.utils.util.StringUtils.getString(this)
}

fun Int.color(): Int {
    return com.zhj.bluetooth.sdkdemo.utils.util.ColorUtils.getColor(this)
}

fun Int.string(vararg any: Any): String {
    return String.format(com.zhj.bluetooth.sdkdemo.utils.util.StringUtils.getString(this), *any)
}

fun Int.string(context: Context, vararg any: Any): String {
    return String.format(context.getString(this), *any)
}

fun <T : ViewDataBinding> DataBindingEpoxyModel.DataBindingHolder.get(): T {
    return this.dataBinding as T
}

inline fun <reified T> T.deepCopy(): T {
    return com.zhj.bluetooth.sdkdemo.utils.util.GsonUtils.fromJson(com.zhj.bluetooth.sdkdemo.utils.util.GsonUtils.toJson(this), T::class.java)
}

fun setVisibility(isVisibility: Boolean, vararg views: View) {
    views.forEach {
        it.visibility = if (isVisibility) View.VISIBLE else View.GONE
    }
}

/*fun <V : Any> args() = object : ReadOnlyProperty<Fragment, V> {
    var value: V? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): V {
        if (value == null) {
            val args = thisRef.arguments
                ?: throw IllegalArgumentException("There are no fragment arguments!")
            val argUntyped = args.get(KEY_ARG)
            argUntyped ?: throw IllegalArgumentException("arguments not found at key KEY_ARG!")
            @Suppress("UNCHECKED_CAST")
            value = argUntyped as V
        }
        return value ?: throw IllegalArgumentException("")
    }
}*/

/**
 * 以下这个录制视频用
 */
private val viewMap = WeakHashMap<View, Long>()
fun View.isUsefulClick(duration: Int = 1000): Boolean {
    if (viewMap.containsKey(this)) {
        if (System.currentTimeMillis().minus(viewMap[this] ?: 0L) < duration) {
            return false
        }
    }
    viewMap.clear()
    viewMap[this] = System.currentTimeMillis()
    return true
}