package com.paddington.dev

import android.content.Intent
import androidx.annotation.Px
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.paddington.dev.loader.ImageLoaderEngine
import com.paddington.dev.selector.PictureSelectorActivity
import com.paddington.dev.selector.PictureSelectorFragment

/**
 * <pre>
 *     author : paddington
 *     e-mail : paddington.699@gmail.com
 *     time   : 2021/01/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class MediaSelector {
    companion object {
        const val KEY_MEDIA_RESULT = "key_media_result"
    }

    init {
        MediaSelectorConfig.reset()
    }

    fun setImageLoader(imageLoader: ImageLoaderEngine): MediaSelector {
        MediaSelectorConfig.imageLoaderEngine = imageLoader
        return this
    }

    fun setSelectType(type: SelectType): MediaSelector {
        MediaSelectorConfig.selectType = type
        return this
    }

    fun setMaxSelectCount(maxCount: Int): MediaSelector {
        MediaSelectorConfig.maxSelectCount = maxCount
        return this
    }

    fun setSpanCount(spanCount: Int): MediaSelector {
        MediaSelectorConfig.spanCount = spanCount
        return this
    }

    fun setGsyLayoutId(gsyLayoutId: Int): MediaSelector {
        MediaSelectorConfig.gsyLayoutId = gsyLayoutId
        return this
    }

    fun forResult(activity: FragmentActivity, requestCode: Int, autoClose: Boolean = true) {
        MediaSelectorConfig.autoClose = autoClose
        val intent = Intent(activity, PictureSelectorActivity::class.java)
        activity.startActivityForResult(intent, requestCode)
    }

    fun forResult(fragment: Fragment, requestCode: Int, autoClose: Boolean = true) {
        MediaSelectorConfig.autoClose = autoClose
        val intent = Intent(fragment.requireContext(), PictureSelectorActivity::class.java)
        fragment.startActivityForResult(intent, requestCode)
    }

    fun getFragment(
        listener: MediaSelectorListener,
        @Px bottomPadding: Int = 0
    ): PictureSelectorFragment {
        return PictureSelectorFragment().apply {
            setMediaSelectListener(listener)
            arguments = bundleOf(
                PictureSelectorFragment.BOTTOM_PADDING to bottomPadding
            )
        }
    }
}