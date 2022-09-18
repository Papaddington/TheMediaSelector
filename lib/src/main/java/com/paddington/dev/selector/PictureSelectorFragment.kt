package com.paddington.dev.selector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.lxj.xpermission.PermissionConstants
import com.lxj.xpermission.XPermission
import com.paddington.dev.MediaSelectorConfig
import com.paddington.dev.MediaSelectorListener
import com.paddington.dev.R
import com.paddington.dev.base.PictureSelectEventDispatcher
import com.paddington.dev.bean.MediaEntity
import com.paddington.dev.preview.PreviewActivity
import com.paddington.dev.scan.MediaScanner
import com.paddington.dev.utils.dpf2pxf
import com.paddington.dev.widget.GridItemDecoration
import kotlinx.android.synthetic.main.base_ms_fragment_picture_selector.*

/**
 * <pre>
 *     author : paddington
 *     e-mail : paddington.699@gmail.com
 *     time   : 2021/01/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class PictureSelectorFragment : Fragment(),
    PictureSelectEventDispatcher.OnPictureSelectChangListener {
    companion object {
        //recyclerView底部的padding
        const val BOTTOM_PADDING = "bottom_padding"
    }

    private var mMediaSelectorListener: MediaSelectorListener? = null
    private var mSelectMediaList: List<MediaEntity>? = null
    private var mCurrentFolderList: List<MediaEntity>? = null
    private lateinit var mMediaScanner: MediaScanner
    private lateinit var mPictureSelectorAdapter: PictureSelectorAdapter
    private lateinit var mFolderSelectorPopWindow: FolderSelectorPopWindow
    private var mBottomPadding = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.base_ms_fragment_picture_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PictureSelectEventDispatcher.register(this)
        arguments?.let {
            mBottomPadding = it.getInt(BOTTOM_PADDING, 0)
        }
        initPage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        PictureSelectEventDispatcher.unRegister(this)
    }

    private fun initPage() {
        mMediaScanner = MediaScanner(requireContext(), MediaSelectorConfig.selectType)
        initRecyclerView()
        initActionBar()
        initFolderSelectorView()

        checkPermissionAndScan()
    }

    private fun initFolderSelectorView() {
        mFolderSelectorPopWindow = FolderSelectorPopWindow(requireContext())
        mFolderSelectorPopWindow.setFolderSelectListener {
            mPictureSelectorAdapter.setNewData(it.mediaList)
            mCurrentFolderList = it.mediaList
            tvFolderName.text = it.name
            mFolderSelectorPopWindow.dismiss()
        }
    }

    private fun initRecyclerView() {
        mPictureSelectorAdapter =
            PictureSelectorAdapter(
                requireContext(),
                MediaSelectorConfig.selectType,
                MediaSelectorConfig.maxSelectCount
            )
        mPictureSelectorAdapter.selectStateListener =
            object : PictureSelectorAdapter.PictureSelectorListener {
                override fun selectedChanged(mediaList: List<MediaEntity>) {
                    tvNext.isEnabled = mediaList.isNotEmpty()
                    mSelectMediaList = mediaList
                }

                override fun selectLimit() {
                    Toast.makeText(
                        requireContext(),
                        "至多选中${MediaSelectorConfig.maxSelectCount}张图片",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onMediaItemClick(mediaEntity: MediaEntity, position: Int) {
                    jump2PreviewActivity(position)
                }
            }
        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), MediaSelectorConfig.spanCount)
            addItemDecoration(
                GridItemDecoration(
                    MediaSelectorConfig.spanCount,
                    context.dpf2pxf(1.5f).toInt()
                )
            )
            val itemAnimator = itemAnimator as DefaultItemAnimator
            itemAnimator.supportsChangeAnimations = false
            adapter = mPictureSelectorAdapter
            if (mBottomPadding != 0) {
                updatePadding(bottom = mBottomPadding)
                clipToPadding = true
            }

        }
    }

    private fun initActionBar() {
        imgClose.setOnClickListener {
            requireActivity().finish()
        }

        tvNext.setOnClickListener {
            clickNext()
        }

        llFolderName.setOnClickListener {
            openFolderSelectMenu()
        }
    }

    /**
     * 校验权限并且扫描
     */
    private fun checkPermissionAndScan() {
        XPermission.create(requireContext(), PermissionConstants.STORAGE)
            .callback(
                object : XPermission.SimpleCallback {
                    override fun onGranted() {
                        scan()
                    }

                    override fun onDenied() {
                    }
                }
            )
            .request()
    }

    /**
     * 扫描媒体库
     */
    private fun scan() {
        mMediaScanner.scan { folderList ->
            mFolderSelectorPopWindow.setNewData(folderList)
            mCurrentFolderList = folderList[0].mediaList
            mPictureSelectorAdapter.setNewData(mCurrentFolderList ?: emptyList())
            tvFolderName.text = folderList[0].name
        }
    }

    private fun openFolderSelectMenu() {
        if (mFolderSelectorPopWindow.isShowing) {
            mFolderSelectorPopWindow.dismiss()
        } else {
            mFolderSelectorPopWindow.showAsDropDown(flActionBar)
        }
    }

    private fun clickNext() {
        mSelectMediaList?.let { selectedList ->
            mMediaSelectorListener?.onResult(selectedList)
        }
    }

    private fun jump2PreviewActivity(pos: Int) {
        mCurrentFolderList?.let {
            PreviewActivity.start(
                requireContext(),
                it,
                pos,
                mSelectMediaList ?: emptyList()
            )
        }
    }

    fun setMediaSelectListener(mediaListener: MediaSelectorListener) {
        mMediaSelectorListener = mediaListener
    }

    override fun onChanged(event: PictureSelectEventDispatcher.MediaSelectChangedEvent) {
        if (event.isSelect) {
            mPictureSelectorAdapter.selectMedia(event.mediaEntity)
        } else {
            mPictureSelectorAdapter.unSelectMedia(event.mediaEntity)
        }
    }
}