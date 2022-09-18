package com.paddington.dev

import com.paddington.dev.bean.MediaEntity

/**
 * <pre>
 *     author : paddington
 *     e-mail : paddington.699@gmail.com
 *     time   : 2021/01/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */
fun interface MediaSelectorListener {
    fun onResult(mediaList: List<MediaEntity>)
}