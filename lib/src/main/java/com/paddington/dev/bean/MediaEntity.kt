package com.paddington.dev.bean

import android.net.Uri
import android.os.Parcelable
import com.paddington.dev.utils.Utils
import kotlinx.android.parcel.Parcelize
import java.io.File

/**
 * <pre>
 *     author : paddington
 *     e-mail : paddington.699@gmail.com
 *     time   : 2020/05/21
 *     desc   : 媒体资源封装类
 *     version: 1.0
 * </pre>
 */
@Parcelize
data class MediaEntity(
        val id: Long,
        val path: String,
        val size: Long = 0L,
        val width: Int = 0,
        val height: Int = 0,
        val type: MediaType = MediaType.PICTURE,
        val duration: Long = 0L,
        var isCheck: Boolean = false
) : Parcelable {

    /**
     * 是否是长图
     */
    fun isLongPicture(): Boolean {
        val h = width * 3
        return height > h
    }

    /**
     * 获取媒体的uri
     * @return Uri
     */
    fun getUriPath(): Uri {
        return if (Utils.isAndroidQ()) Uri.parse(path) else Uri.fromFile(File(path))
    }

    /**
     * 由于id是唯一的 所以重写equals 和 hashCode
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MediaEntity
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}

/**
 * 媒体类型
 */
enum class MediaType {

    /**
     * 视频
     */
    VIDEO,

    /**
     * 图片
     */
    PICTURE
}