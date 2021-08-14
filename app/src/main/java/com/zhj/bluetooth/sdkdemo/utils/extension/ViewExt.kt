package com.zhj.bluetooth.sdkdemo.utils.extension

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.zhj.bluetooth.sdkdemo.utils.listener.SafeClickListener


fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

fun View.setHeight(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.height = value
        layoutParams = lp
    }
}

fun View.setWidth(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = value
        layoutParams = lp
    }
}

fun View.resize(width: Int, height: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = width
        lp.height = height
        layoutParams = lp
    }
}

inline val ViewGroup.children: List<View>
    get() = (0 until childCount).map { getChildAt(it) }

operator fun ViewGroup.get(index: Int): View {
    return getChildAt(index)
}

fun TextView.underLine() {
    paint.flags = paint.flags or Paint.UNDERLINE_TEXT_FLAG
    paint.isAntiAlias = true
}

fun TextView.deleteLine() {
    paint.flags = paint.flags or Paint.STRIKE_THRU_TEXT_FLAG
    paint.isAntiAlias = true
}

fun TextView.bold(isBold: Boolean = true) {
    paint.isFakeBoldText = isBold
    paint.isAntiAlias = true
}

fun View.isVisiable(bool: Boolean) {
    visibility = if (bool) View.VISIBLE else View.INVISIBLE
}

fun View.isVisiableOrGone(bool: Boolean) {
    visibility = if (bool) View.VISIBLE else View.GONE
}

fun View.visiable() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}

inline fun View.visiableIf(block: () -> Boolean) {
    if (visibility != View.VISIBLE && block()) {
        visibility = View.VISIBLE
    }
}

fun View.invisiable() {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
}

inline fun View.invisiableIf(block: () -> Boolean) {
    if (visibility != View.INVISIBLE && block()) {
        visibility = View.INVISIBLE
    }
}

fun View.isVisible() = visibility == View.VISIBLE

fun View.isGone() = visibility == View.GONE

fun View.isInvisible() = visibility == View.INVISIBLE

fun View.gone() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

inline fun View.goneIf(block: () -> Boolean) {
    if (visibility != View.GONE && block()) {
        visibility = View.GONE
    }
}

var EditText.value: String
    get() = text.toString()
    set(value) = setText(value)

fun View.getBitmap(): Bitmap {
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    draw(canvas)
    canvas.save()
    return bmp
}


fun EditText.passwordToggledVisible() {
    val selection = selectionStart
    transformationMethod =
            if (transformationMethod == null) PasswordTransformationMethod() else null
    setSelection(selection)
}