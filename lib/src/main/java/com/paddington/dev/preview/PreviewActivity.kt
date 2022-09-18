package com.paddington.dev.preview

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.githang.statusbar.StatusBarCompat
import com.paddington.dev.MediaSelectorConfig
import com.paddington.dev.R
import com.paddington.dev.SelectType
import com.paddington.dev.base.PictureSelectEventDispatcher
import com.paddington.dev.bean.MediaEntity
import com.paddington.dev.bean.MediaType
import kotlinx.android.synthetic.main.base_ms_activity_preview.*

/**
 * <pre>
 *     author : paddington
 *     e-mail : paddington.699@gmail.com
 *     time   : 2021/01/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class PreviewActivity : AppCompatActivity() {
    companion object {
        private const val KEY_POSITION = "key_position"
        private const val KEY_SELECT_MEDIA_LIST = "key_select_media_list"

        /** 解决bundle 大小限制问题 */
        private var previewList: MutableList<MediaEntity>? = null
        fun start(
            context: Context,
            mediaList: List<MediaEntity>,
            position: Int,
            selectMediaList: List<MediaEntity>
        ) {
            val previewIntent = Intent(context, PreviewActivity::class.java)
            previewList = mediaList.toMutableList()
            previewIntent.putExtra(KEY_POSITION, position)
            previewIntent.putExtra(KEY_SELECT_MEDIA_LIST, ArrayList(selectMediaList))
            context.startActivity(previewIntent)
        }
    }

    private lateinit var pageChangeListener: PageChangeListener
    private lateinit var mSelectMediaList: MutableList<MediaEntity>
    private var mCurrentSelectType = MediaType.PICTURE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarCompat.setStatusBarColor(this, Color.BLACK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.BLACK
        }
        setContentView(R.layout.base_ms_activity_preview)

        if (previewList == null) {
            return
        }

        imgBack.setOnClickListener {
            finish()
        }

        initViewPager()

        viewCheckView.setOnClickListener {
            clickCheckView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        vpPreview.unregisterOnPageChangeCallback(pageChangeListener)
        previewList?.clear()
        previewList = null
    }

    private fun initViewPager() {
        val selectMediaList = intent.getParcelableArrayListExtra<MediaEntity>(KEY_SELECT_MEDIA_LIST)
        mSelectMediaList = selectMediaList?.toMutableList() ?: mutableListOf()
        if (mSelectMediaList.isNotEmpty()) {
            mCurrentSelectType = mSelectMediaList[0].type
        }

        val position = intent.getIntExtra(KEY_POSITION, 0)

        val previewAdapter =
            PreviewPagerAdapter(this, previewList!!)

        pageChangeListener = PageChangeListener()

        vpPreview.apply {
            adapter = previewAdapter
            vpPreview.registerOnPageChangeCallback(pageChangeListener)
            setCurrentItem(position, false)
            offscreenPageLimit = 2
        }
    }

    private fun clickCheckView() {
        val position = vpPreview.currentItem
        val media = previewList!![position]
        // 如果已选中且当前类型与选中类型不相同 直接跳出
        if (mSelectMediaList.isNotEmpty()) {
            if (media.type != mCurrentSelectType) {
                Toast.makeText(this, "不能同时选择视频和图片哦~", Toast.LENGTH_SHORT).show()
                return
            } else {
                if (mCurrentSelectType == MediaType.VIDEO &&
                    mSelectMediaList[0].id != media.id
                ) {
                    Toast.makeText(this, "不能同时选择多个视频哦~", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        }
        if (media.isCheck) {
            media.isCheck = false
            mSelectMediaList.remove(media)
            viewCheckView.setCheck(false)
            PictureSelectEventDispatcher.event(
                PictureSelectEventDispatcher.MediaSelectChangedEvent(
                    false,
                    media
                )
            )
        } else {
            // 是否超过最大数量
            val isLimit = mSelectMediaList.size >= MediaSelectorConfig.maxSelectCount
            if (isLimit) {
                Toast.makeText(
                    this,
                    "至多选中${MediaSelectorConfig.maxSelectCount}张图片",
                    Toast.LENGTH_SHORT
                ).show()
                return
            } else {
                mCurrentSelectType = media.type
                media.isCheck = true
                mSelectMediaList.add(media)
                viewCheckView.setCheck(media.isCheck, mSelectMediaList.size)
                PictureSelectEventDispatcher.event(
                    PictureSelectEventDispatcher.MediaSelectChangedEvent(
                        true,
                        media
                    )
                )
            }
        }
        finish()
    }

    private fun pageSelected(position: Int) {
        if (previewList == null) {
            return
        }
        val item = previewList!![position]
        val isChooseType =
            (MediaSelectorConfig.selectType == SelectType.IMAGE && item.type == MediaType.PICTURE)
                || (MediaSelectorConfig.selectType == SelectType.VIDEO && item.type == MediaType.VIDEO)
                || MediaSelectorConfig.selectType == SelectType.ALL
        if (isChooseType) {
            viewCheckView.setCheck(item.isCheck, mSelectMediaList.indexOf(item) + 1)
            viewCheckView.isEnabled = true
        } else {
            viewCheckView.setCheck(false)
            viewCheckView.isEnabled = false
        }
    }

    private inner class PageChangeListener : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            pageSelected(position)
        }
    }
}