package com.paddington.dev.utils

import android.content.Context
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * <pre>
 *     author : paddington
 *     e-mail : paddington.699@gmail.com
 *     time   : 2020/06/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */

internal fun Long.toDateString(): String {
    return String.format(
            Locale.CHINA, "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(this), TimeUnit.MILLISECONDS.toSeconds(this)
            - TimeUnit.MINUTES.toSeconds(
            TimeUnit.MILLISECONDS.toMinutes(this)
    ))
}

/**
 * 获取屏幕高度
 */
internal val Context.displayHeight: Int
    get() {
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager?
                ?: return 0
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }

/**
 * 获取屏幕宽度
 */
internal val Context.displayWidth: Int
    get() {
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager?
                ?: return 0
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

internal fun Context.dp2px(dpValue: Float): Int {
    return (0.5f + dpValue * resources.displayMetrics.density).toInt()
}

internal fun Context.px2dp(pxValue: Int): Float {
    return pxValue / resources.displayMetrics.density
}

internal fun RecyclerView.asVertical() {
    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
}

internal fun RecyclerView.asHorizontal() {
    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
}

internal fun TextView.updateDrawable(top: Drawable? = null, left: Drawable? = null, bottom: Drawable? = null, right: Drawable? = null) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(left, top, right, bottom)
}

internal inline fun Cursor.forEach(block: (Cursor) -> Unit) {
    do {
        block(this)
    } while (moveToNext())
}

/**
 * dp转px
 */
internal fun Context.dpf2pxf(dpValue: Float): Float {
    if (dpValue == 0f) return 0f
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f)
}