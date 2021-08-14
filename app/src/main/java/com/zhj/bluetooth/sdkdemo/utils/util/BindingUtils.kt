package com.zhj.bluetooth.sdkdemo.utils.util

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.zhj.bluetooth.sdkdemo.utils.listener.OnSingleClickListener
import java.io.File


@BindingAdapter("adImageUrl")
fun setAdImageContent(iv: ImageView, url: String?) {
    if (url.isNullOrEmpty()) return
    val request: RequestBuilder<out Drawable> = if (url.endsWith("gif")) {
        Glide.with(iv.context).asGif().fitCenter().priority(Priority.IMMEDIATE)
    } else {
        Glide.with(iv.context).asDrawable().priority(Priority.IMMEDIATE)
    }
    request.load(url)
//        .fallback(R.drawable.welcome_center_img)
        .into(iv)
}

@BindingAdapter("imageUrl")
fun setImageContent(iv: ImageView, url: String?) {
    Glide.with(iv.context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade()) //淡入淡出
        .into(iv)
}

@BindingAdapter("centerCrop")
fun setCenterCropImage(iv: ImageView, url: String?) {
    Glide.with(iv.context)
        .load(url)
        .signature(ObjectKey(url.toString()))
        .fitCenter()
        .transition(DrawableTransitionOptions.withCrossFade()) //淡入淡出
        .into(iv)
}

@BindingAdapter("imageUrl")
fun setImageContent(iv: ImageView, drawable: Int?) {
    Glide.with(iv.context)
        .load(drawable)
        .skipMemoryCache(false)
        .dontAnimate()
        .into(iv)
}

@BindingAdapter("clearCacheUrl")
fun setNoCacheImage(iv: ImageView, url: String?, tag: String) {
    Glide.with(iv.context)
        .load(url)
        .signature(ObjectKey(tag))
        .transition(DrawableTransitionOptions.withCrossFade()) //淡入淡出
        .into(iv)
}

/**
 * 显示本地视频的缩列图,因扫描本地视频获取不到thumb path则以下处理
 * thumbPath 为空时，使用videoPath
 * 使用[0] 为thumbPath,[1] videoPath
 */
@BindingAdapter("videoThumbImage")
fun setVideoThrumb(iv: ImageView, thumb: Array<String>?) {
    if (thumb.isNullOrEmpty() || thumb.size < 2) {
        return
    }
    if (thumb[0].isEmpty() || !File(thumb[0]).exists()) {
        Glide.with(iv.context)
            .load(Uri.fromFile(File(thumb[1]))).apply(
                RequestOptions()
//                    .placeholder(R.drawable.bg_load_local_video)
                    .centerCrop()
            )
            .into(iv)
    } else {
        Glide.with(iv.context)
            .load(thumb[0])
            .skipMemoryCache(false)
//            .placeholder(R.drawable.bg_load_local_video)
            .dontAnimate()
            .into(iv)
    }

}

@BindingAdapter("headUrl")
fun setHeadImage(iv: ImageView, url: String?) {
    Glide.with(iv.context)
        .load(url)
        .skipMemoryCache(false)
        .dontAnimate()
//        .placeholder(R.drawable.empty_head)
//        .error(R.drawable.empty_head)
        .into(iv)
}

@BindingAdapter("addImageUrl")
fun setAddImageContent(iv: ImageView, url: String?) {
    Glide.with(iv.context)
        .load(url)
        .skipMemoryCache(false)
        .dontAnimate()
        .into(iv)
}

@BindingAdapter("onSingleClick")
fun View.setOnSingleClickListener(clickListener: View.OnClickListener?) {
    clickListener?.also {
        setOnClickListener(OnSingleClickListener(it))
    } ?: setOnClickListener(null)
}

@BindingAdapter("onSingleClickWithAnim")
fun View.setOnSingleClickListenerWithAnim(clickListener: View.OnClickListener?) {
    clickListener?.also {
        setOnClickListener(OnSingleClickListener(it, hasAnimation = true))
    } ?: setOnClickListener(null)
}

@BindingAdapter("ifSelected")
fun ifSelected(view: View, isSelected: Boolean?) {
    if (isSelected != null)
        view.isSelected = isSelected
}


@BindingAdapter("android:src")
fun setImageUri(view: ImageView, imageUri: String?) {
    if (imageUri == null) {
        view.setImageURI(null)
    } else {
        view.setImageURI(Uri.parse(imageUri))
    }
}

@BindingAdapter("android:src")
fun setImageUri(view: ImageView, imageUri: Uri?) {
    view.setImageURI(imageUri)
}

@BindingAdapter("android:src")
fun setImageDrawable(view: ImageView, drawable: Drawable?) {
    view.setImageDrawable(drawable)
}

@BindingAdapter("android:src")
fun setImageResource(imageView: ImageView, resource: Int) {
    Glide.with(imageView.context)
        .load(resource)
        .skipMemoryCache(false)
        .dontAnimate()
        .into(imageView)
}

@BindingAdapter("ifVisibleGone")
fun ifVisibleGone(view: View, isVisible: Boolean) {
    view.visibility = when (isVisible) {
        true -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("ifVisibleInvisible")
fun ifVisibleInvisible(view: View, isVisible: Boolean) {
    view.visibility = when (isVisible) {
        true -> View.VISIBLE
        else -> View.INVISIBLE
    }
}


@BindingAdapter("grayImage")
fun grayImage(imageView: ImageView, ifGray: Boolean) {
    if (ifGray) {
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)
        val filter = ColorMatrixColorFilter(matrix)
        imageView.colorFilter = filter
    } else {
        imageView.clearColorFilter()
    }
}

