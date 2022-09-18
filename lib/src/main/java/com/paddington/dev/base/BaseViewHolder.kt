package com.paddington.dev.base

import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * <pre>
 *     author : paddington
 *     e-mail : paddington.699@gmail.com
 *     time   : 2021/01/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val views = SparseArray<View>()

    fun getView(id: Int): View? {
        var view = views[id]
        if (view == null) {
            view = itemView.findViewById(id)
            views.put(id, view)
        }
        return view
    }

}