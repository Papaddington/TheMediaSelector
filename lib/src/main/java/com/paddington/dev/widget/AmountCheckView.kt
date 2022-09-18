package com.paddington.dev.widget;

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.paddington.dev.R
import kotlinx.android.synthetic.main.base_ms_view_amount_check.view.*

/**
 * <pre>
 *     author : paddington
 *     e-mail : paddington.699@gmail.com
 *     time   : 2020/05/26
 *     desc   : 带数量的选择器
 *     version: 1.0
 * </pre>
 */
class AmountCheckView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context)
                .inflate(R.layout.base_ms_view_amount_check, this, true)
    }

    fun setCheck(isCheck: Boolean, checkAmount: Int = 0) {
        if (isCheck) {
            imgCheckView.visibility = View.GONE
            tvSelectSortView.visibility = View.VISIBLE
            tvSelectSortView.text = checkAmount.toString()
        } else {
            imgCheckView.visibility = View.VISIBLE
            tvSelectSortView.visibility = View.GONE
        }
    }
}