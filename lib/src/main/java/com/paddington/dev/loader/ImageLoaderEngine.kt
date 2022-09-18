package com.paddington.dev.loader

import android.content.Context
import android.widget.ImageView

/**
 * <pre>
 *     author : paddington
 *     e-mail : paddington.699@gmail.com
 *     time   : 2021/01/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface ImageLoaderEngine {
    fun loadListImage(context: Context, url: String, image: ImageView)

    fun loadImage(context: Context, url: String, image: ImageView)

    fun loadListGif(context: Context, url: String, image: ImageView)

    fun loadGifImage(context: Context, url: String, image: ImageView)

    fun loadFolderImage(context: Context, url: String, image: ImageView)
}