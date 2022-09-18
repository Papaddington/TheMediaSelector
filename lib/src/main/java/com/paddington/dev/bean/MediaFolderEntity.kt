package com.paddington.dev.bean

/**
 * <pre>
 *     author : paddington
 *     e-mail : paddington.699@gmail.com
 *     time   : 2021/01/22
 *     desc   : 媒体目录类
 *     version: 1.0
 * </pre>
 */
data class MediaFolderEntity(
    val name: String,
    val mediaList: MutableList<MediaEntity>,
    val isCheck: Boolean = false
) {

    fun getFirstImagePath(): String? {
        return this.mediaList.firstOrNull()?.path
    }

    fun getMediaCount(): Int {
        return this.mediaList.size
    }
}