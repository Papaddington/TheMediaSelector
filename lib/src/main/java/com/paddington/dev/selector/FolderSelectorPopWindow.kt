package com.paddington.dev.selector

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.PopupWindow
import com.paddington.dev.bean.MediaFolderEntity
import com.paddington.dev.utils.displayHeight
import com.paddington.dev.utils.displayWidth

/**
 * <pre>
 *     author : paddington
 *     e-mail : paddington.699@gmail.com
 *     time   : 2021/01/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class FolderSelectorPopWindow(val context: Context) : PopupWindow(context) {

    private val folderSelectorView = FolderSelectorView(context)

    init {
        contentView = folderSelectorView
        width = context.displayWidth
        setBackgroundDrawable(ColorDrawable(Color.BLACK))
        // isOutsideTouchable = true
    }

    fun setFolderSelectListener(block: (MediaFolderEntity) -> Unit) {
        folderSelectorView.folderSelectListener = block
    }

    fun setNewData(folderList: MutableList<MediaFolderEntity>) {
        folderSelectorView.setNewData(folderList)
    }

    override fun showAsDropDown(anchor: View?) {
        val rect = Rect()
        anchor!!.getGlobalVisibleRect(rect)
        val h: Int = context.displayHeight - anchor.measuredHeight
        height = h
        super.showAsDropDown(anchor)
    }
}