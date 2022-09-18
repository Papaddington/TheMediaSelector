package com.paddington.dev.selector

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.paddington.dev.MediaSelectorConfig
import com.paddington.dev.R
import com.paddington.dev.SelectType
import com.paddington.dev.base.BaseRecyclerViewAdapter
import com.paddington.dev.base.BaseViewHolder
import com.paddington.dev.bean.MediaEntity
import com.paddington.dev.bean.MediaType
import com.paddington.dev.loader.ImageLoaderEngine
import com.paddington.dev.utils.toDateString
import kotlinx.android.synthetic.main.base_ms_item_media_select_picture.view.*

class PictureSelectorAdapter(
    private val mContext: Context,
    private val mSelectType: SelectType,
    private val mMaxSelectCount: Int = 9
) :
    BaseRecyclerViewAdapter<MediaEntity>(mContext, mutableListOf()) {

    private var mCurrentSelectMediaType: MediaType = MediaType.PICTURE
    private var mSelectedMediaList = mutableListOf<MediaEntity>()
    var selectStateListener: PictureSelectorListener? = null

    private val mImageLoader: ImageLoaderEngine = MediaSelectorConfig.getImageLoader()

    override val layoutRes: Int = R.layout.base_ms_item_media_select_picture

    @SuppressLint("ClickableViewAccessibility")
    override fun convert(holder: BaseViewHolder, item: MediaEntity) {
        with(holder.itemView) {
            mImageLoader.loadListImage(mContext, item.path, ivCover)
            val isCheck = item.isCheck
            if (isCheck) {
                //当前选中第几个 = 选中列表下标 + 1
                val selectedNum = mSelectedMediaList.indexOf(item) + 1
                viewAmountCheck.setCheck(true, selectedNum)
            } else {
                viewAmountCheck.setCheck(false)
            }

            if (item.type == MediaType.VIDEO) {
                tvDuration.visibility = View.VISIBLE
                tvDuration.text = item.duration.toDateString()
            } else {
                tvDuration.visibility = View.GONE
            }

            viewMask.setOnTouchListener { _, _ ->
                return@setOnTouchListener true
            }
            val isChoose = (mSelectType == SelectType.IMAGE && item.type == MediaType.PICTURE)
                || (mSelectType == SelectType.VIDEO && item.type == MediaType.VIDEO)
                || mSelectType == SelectType.ALL
            if (isChoose) {
                val hasSelect = mSelectedMediaList.isNotEmpty()
                val isDiff = item.type != mCurrentSelectMediaType
                if (hasSelect) {
                    if (isDiff) {
                        viewMask.visibility = View.VISIBLE
                    } else {
                        if (mCurrentSelectMediaType == MediaType.VIDEO) {
                            if (mSelectedMediaList[0].id == item.id) {
                                viewMask.visibility = View.GONE
                            } else {
                                viewMask.visibility = View.VISIBLE
                            }
                        } else {
                            viewMask.visibility = View.GONE
                        }
                    }
                } else {
                    viewMask.visibility = View.GONE
                }
            } else {
                viewMask.visibility = View.GONE
            }

            viewAmountCheck.setOnClickListener {
                checkAction(item)
            }

            setOnClickListener {
                selectStateListener?.onMediaItemClick(item, holder.adapterPosition)
            }

        }
    }

    private fun checkAction(item: MediaEntity) {
        val isCheck = item.isCheck
        val isLimit = mSelectedMediaList.size >= mMaxSelectCount
        when {
            isCheck -> {
                realUnCheckAction(item)
            }
            isLimit -> {
                selectStateListener?.selectLimit()
            }
            else -> {
                realCheckAction(item)
            }
        }
    }

    /**
     * 选中
     * @param item MediaEntity
     */
    private fun realCheckAction(item: MediaEntity) {
        item.isCheck = true
        mSelectedMediaList.add(item)
        mCurrentSelectMediaType = item.type

        notifyDataSetChanged()
        selectStateListener?.selectedChanged(mSelectedMediaList)
    }

    /**
     * 取消选中
     * @param item MediaEntity
     */
    private fun realUnCheckAction(item: MediaEntity) {
        mSelectedMediaList.remove(item)
        item.isCheck = false
        notifyDataSetChanged()
        selectStateListener?.selectedChanged(mSelectedMediaList)
    }

    /**
     * 选中媒体
     * @param mediaEntity MediaEntity
     */
    fun selectMedia(mediaEntity: MediaEntity) {
        mData.find {
            it.id == mediaEntity.id
        }?.let {
            realCheckAction(it)
        }
    }

    /**
     * 取消选中媒体
     * @param mediaEntity MediaEntity
     */
    fun unSelectMedia(mediaEntity: MediaEntity) {
        mData.find {
            it.id == mediaEntity.id
        }?.let {
            realUnCheckAction(it)
        }
    }

    interface PictureSelectorListener {

        /**
         * 选中数量发生变化
         * @param mediaList List<MediaEntity>
         */
        fun selectedChanged(mediaList: List<MediaEntity>)

        /**
         * 选中数量已满
         */
        fun selectLimit()

        /**
         * 点击item
         * @param mediaEntity MediaEntity
         */
        fun onMediaItemClick(
            mediaEntity: MediaEntity,
            position: Int
        )
    }
}