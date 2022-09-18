package com.paddington.dev.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
abstract class BaseRecyclerViewAdapter<T>(
    private val mContext: Context,
    var mData: List<T> = mutableListOf(),
) : RecyclerView.Adapter<BaseViewHolder>() {

    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemChildClickListener: OnItemChildClickListener? = null

    private val mChildClickViewIds = linkedSetOf<Int>()

    abstract val layoutRes: Int

    abstract fun convert(holder: BaseViewHolder, item: T)

    open fun convert(holder: BaseViewHolder, item: T, payloads: MutableList<Any>) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(mContext).inflate(layoutRes, parent, false)
        val holder = BaseViewHolder(view)
        bindClickListener(holder)
        return holder
    }

    private fun bindClickListener(holder: BaseViewHolder) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener {
                mOnItemClickListener!!.onItemClick(this, it, holder.adapterPosition)
            }
        }
        if (mOnItemChildClickListener != null) {
            mChildClickViewIds.forEach { id ->
                holder.getView(id)?.setOnClickListener {
                    mOnItemChildClickListener!!.onItemChildClick(this, it, holder.adapterPosition)
                }
            }
        }
    }

    private fun addChildClickListener(id: Int) {
        mChildClickViewIds.add(id)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (getItem(position) != null) {
            convert(holder, getItem(position)!!)
        }
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if (getItem(position) != null) {
            convert(holder, getItem(position)!!, payloads)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun getItem(position: Int): T? {
        val list = mData
        if (position >= list.size || position < 0) {
            return null
        }
        return list[position]
    }

    fun setNewData(data: List<T>) {
        mData = data
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(adapter: BaseRecyclerViewAdapter<*>, itemView: View, pos: Int)
    }

    interface OnItemChildClickListener {
        fun onItemChildClick(adapter: BaseRecyclerViewAdapter<*>, childView: View, pos: Int)
    }
}
