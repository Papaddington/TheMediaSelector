package com.paddington.dev.utils

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import androidx.loader.content.CursorLoader
import java.io.File

/**
 * <pre>
 *     author : paddington
 *     e-mail : paddington.699@gmail.com
 *     time   : 2021/01/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
internal object Utils {
    private const val MIME_TYPE_PREFIX_IMAGE = "image"
    private const val MIME_TYPE_PREFIX_VIDEO = "video"
    private const val MIME_TYPE_PREFIX_AUDIO = "audio"

    fun isAndroidQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    fun isVideo(mimeType: String): Boolean {
        return mimeType.startsWith(MIME_TYPE_PREFIX_VIDEO)
    }

    fun getAndroidQPath(id: Long): String {
        val uri = MediaStore.Files.getContentUri("external")
        return uri.buildUpon().appendPath(id.toString()).build().toString()
    }

    fun getLocalDuration(
        context: Context,
        uri: Uri
    ): Long {
        return try {
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(context, uri)
            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    fun getLocalDuration(path: String): Long {
        return try {
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(path)
            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param path
     * @return
     */
    fun isFileExists(path: String): Boolean {
        return !(!TextUtils.isEmpty(path) && !File(path).exists())
    }

    fun checkNull(any: Any?) {
        if (any == null) {
            throw IllegalArgumentException("该参数不能为空")
        }
    }

    /**
     * 获取文件真实路径 GSY无法播放content://
     * @param context Context
     * @param path String
     * @return String
     */
    fun getRealPath(context: Context, path: String): String {
        return when {
            path.startsWith("content") -> {
                getRealPathFromUriByContent(context, Uri.parse(path))
            }
            path.startsWith("file") -> {
                getRealPathFromUriByFile(Uri.parse(path))
            }
            else -> {
                path
            }
        }
    }

    private fun getRealPathFromUriByFile(uri: Uri): String {
        val uri2Str = uri.toString()
        return uri2Str.substring(uri2Str.indexOf(":") + 3)
    }

    private fun getRealPathFromUriByContent(context: Context, uri: Uri): String {
        var filePath = ""
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        val loader = CursorLoader(context, uri, projection, null, null, null)

        val cursor = loader.loadInBackground()

        if (cursor != null) {
            cursor.moveToFirst()
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]))
            cursor.close()
        }
        return filePath
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId: Int =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}