package com.paddington.dev.widget.video

import android.content.Context
import android.util.AttributeSet
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.paddington.dev.MediaSelectorConfig

/**
 * <pre>
 *     author : paddington
 *     e-mail : paddington.699@gmail.com
 *     time   : 2021/01/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class PreviewGSYVideoView : StandardGSYVideoPlayer {
    constructor(context: Context) : super(context)

    constructor(context: Context, fullFlag: Boolean) : super(context, fullFlag)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun getLayoutId(): Int {
        return if (MediaSelectorConfig.gsyLayoutId == 0) {
            super.getLayoutId()
        } else {
            MediaSelectorConfig.gsyLayoutId
        }
    }
}