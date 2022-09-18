package com.paddington.dev.scan

import android.content.AsyncQueryHandler
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.paddington.dev.SelectType
import com.paddington.dev.bean.MediaEntity
import com.paddington.dev.bean.MediaFolderEntity
import com.paddington.dev.bean.MediaType
import com.paddington.dev.utils.Utils
import com.paddington.dev.utils.forEach

/**
 * <pre>
 *     author : paddington
 *     e-mail : paddington.699@gmail.com
 *     time   : 2020/05/21
 *     desc   : 媒体资源扫描类
 *     version: 1.0
 * </pre>
 */
class MediaScanner(private val context: Context, private val type: SelectType) {

    companion object {
        private val QUERY_URI = MediaStore.Files.getContentUri("external")

        /**
         * 媒体文件数据库字段
         */
        private val PROJECTION = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.WIDTH,
            MediaStore.MediaColumns.HEIGHT,
            MediaStore.MediaColumns.DURATION,
            MediaStore.MediaColumns.SIZE,
            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME
        )

        private val SECTION_ARGS = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
        )

        private val IMAGE_ARGS = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()
        )

        private val VIDEO_ARGS = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
        )

        private const val ORDER_BY = MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
    }

    fun scan(
        listener: ((MutableList<MediaFolderEntity>) -> Unit)? = null
    ) {
        val scanHandler = ScanAsyncQueryHandler(context, listener)
        scanHandler.startQuery(
            0x12,
            null,
            QUERY_URI,
            PROJECTION,
            getSelection(),
            null,
            ORDER_BY
        )
    }

    private fun getSelection(): String? {
        return when (type) {
            SelectType.ALL -> getAllSelection()
            SelectType.IMAGE -> getImageSelection()
            SelectType.VIDEO -> getVideoSelection()
        }
    }

    /**
     * 根据当前选中模式,筛选图片还是视频
     * @return Array<String>
     */
    private fun getSelectionArgs(): Array<String> {
        return when (type) {
            SelectType.VIDEO -> VIDEO_ARGS
            SelectType.IMAGE -> IMAGE_ARGS
            SelectType.ALL -> SECTION_ARGS
        }
    }

    private fun getVideoSelection(): String {
        return "${MediaStore.Files.FileColumns.MEDIA_TYPE}=${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO}" +
                " AND ${getDurationCondition(Long.MAX_VALUE, 3000L)}" +
                " AND ${MediaStore.MediaColumns.SIZE}>0"
    }

    private fun getImageSelection(): String {
        return "${MediaStore.Files.FileColumns.MEDIA_TYPE}=${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE}" +
                " AND ${MediaStore.MediaColumns.MIME_TYPE}!='image/gif'" +
                " AND ${MediaStore.MediaColumns.SIZE}>0"
    }

    private fun getAllSelection(): String {
        return getVideoSelection() + " OR " + getImageSelection()
    }

    /**
     * 获取视频(最长或最小时间)
     */
    private fun getDurationCondition(
        max: Long,
        min: Long
    ): String? {
        return "${MediaStore.MediaColumns.DURATION}>=${min}" +
                " AND ${MediaStore.MediaColumns.DURATION}<=${max}"
    }

    class ScanAsyncQueryHandler(
        private val context: Context,
        private val listener: ((MutableList<MediaFolderEntity>) -> Unit)? = null
    ) : AsyncQueryHandler(context.contentResolver) {

        override fun onQueryComplete(token: Int, cookie: Any?, cursor: Cursor?) {
            super.onQueryComplete(token, cookie, cursor)
            convertCursor(cursor)
        }

        private fun convertCursor(cursor: Cursor?) {
            val count = cursor?.count ?: 0
            if (count > 0) {
                cursor?.moveToFirst()
                try {
                    val folderList = mutableListOf<MediaFolderEntity>()
                    val allMediaList = mutableListOf<MediaEntity>()
                    cursor?.forEach {
                        val id = it.getLong(it.getColumnIndexOrThrow(PROJECTION[0]))

                        val absolutePath = it.getString(it.getColumnIndexOrThrow(PROJECTION[1]))
                        val path =
                            if (Utils.isAndroidQ()) Utils.getAndroidQPath(id) else absolutePath

                        val mimeType = it.getString(it.getColumnIndexOrThrow(PROJECTION[2]))
                        val width = it.getInt(it.getColumnIndexOrThrow(PROJECTION[3]))
                        val height = it.getInt(it.getColumnIndexOrThrow(PROJECTION[4]))
                        var duration = it.getLong(it.getColumnIndexOrThrow(PROJECTION[5]))
                        val size = it.getLong(it.getColumnIndexOrThrow(PROJECTION[6]))
                        val folderName = it.getString(it.getColumnIndexOrThrow(PROJECTION[7]))
                            ?: "其他"
                        // Log.e(
                        //     "ScanAsyncQueryHandler",
                        //     "id = $id \n" +
                        //         "path = $path \n" +
                        //         "mimeType = $mimeType \n" +
                        //         "width = $width \n" +
                        //         "height = $height \n" +
                        //         "duration = $duration \n" +
                        //         "size = $size \n" +
                        //         "folderName = $folderName \n"
                        // )
                        val mediaEntity = if (Utils.isVideo(mimeType)) {
                            if (duration == 0L) {
                                duration =
                                    if (Utils.isAndroidQ()) Utils.getLocalDuration(path)
                                    else Utils.getLocalDuration(
                                        context,
                                        Uri.parse(path)
                                    )
                            }
                            MediaEntity(
                                id,
                                path,
                                size,
                                type = MediaType.VIDEO,
                                duration = duration
                            )
                        } else MediaEntity(
                            id,
                            path,
                            size,
                            width,
                            height,
                            type = MediaType.PICTURE
                        )
                        allMediaList.add(mediaEntity)
                        //找到列表
                        var folder = folderList.find {
                            it.name == folderName
                        }
                        if (folder == null) {
                            folder = MediaFolderEntity(folderName, mutableListOf())
                            folderList.add(folder)
                        }
                        folder.mediaList.add(mediaEntity)
                    }
                    folderList.add(0, MediaFolderEntity("全部", allMediaList, true))
                    listener?.invoke(folderList)
                } catch (e: Exception) {
                    // Log.e("ScanAsyncQueryHandler", "onError" + e.localizedMessage)
                }
            }
        }
    }
}