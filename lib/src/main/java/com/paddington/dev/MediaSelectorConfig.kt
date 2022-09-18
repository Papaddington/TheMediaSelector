package com.paddington.dev

import com.paddington.dev.loader.ImageLoaderEngine
import com.paddington.dev.utils.Utils

/**
 * <pre>
 *     author : paddington
 *     e-mail : paddington.699@gmail.com
 *     time   : 2021/01/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
internal object MediaSelectorConfig {
    var imageLoaderEngine: ImageLoaderEngine? = null
    var selectType: SelectType = SelectType.ALL
    var maxSelectCount: Int = 9
    var spanCount = 3
    var gsyLayoutId = 0
    var autoClose = true

    fun getImageLoader(): ImageLoaderEngine {
        Utils.checkNull(imageLoaderEngine)
        return imageLoaderEngine!!
    }

    fun reset() {
        imageLoaderEngine = null
        selectType = SelectType.ALL
        maxSelectCount = 9
        spanCount = 3
        gsyLayoutId = 0
        autoClose = true
    }
}