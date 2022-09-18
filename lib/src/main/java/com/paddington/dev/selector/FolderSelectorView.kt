package com.paddington.dev.selector

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paddington.dev.MediaSelectorConfig
import com.paddington.dev.R
import com.paddington.dev.bean.MediaFolderEntity
import kotlinx.android.synthetic.main.base_ms_item_folder_selector.view.*
import kotlinx.android.synthetic.main.base_ms_view_folder_selector.view.*

/**
 * <pre>
 *     author : paddington
 *     e-mail : paddington.699@gmail.com
 *     time   : 2021/01/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class FolderSelectorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var mFolderAdapter: FolderAdapter = FolderAdapter()

    private val mImageLoader = MediaSelectorConfig.getImageLoader()
    private var mFolderList: MutableList<MediaFolderEntity> = mutableListOf()
    var folderSelectListener: ((MediaFolderEntity) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.base_ms_view_folder_selector, this, true)

        recyclerFolder.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mFolderAdapter
        }
    }

    fun setNewData(folderList: MutableList<MediaFolderEntity>) {
        this.mFolderList = folderList
        mFolderAdapter.notifyDataSetChanged()
    }

    private fun clickFolder(mediaFolderEntity: MediaFolderEntity) {
        folderSelectListener?.invoke(mediaFolderEntity)
    }

    inner class FolderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val contentView = LayoutInflater.from(context)
                .inflate(R.layout.base_ms_item_folder_selector, parent, false)
            return ViewHolder(contentView)
        }

        override fun getItemCount() = mFolderList.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val mediaFolderEntity = mFolderList[position]
            val firstImgCoverPath = mediaFolderEntity.getFirstImagePath()
            firstImgCoverPath ?: return
            with(holder.itemView) {
                mImageLoader.loadFolderImage(context, firstImgCoverPath, imgCover)
                tvFolderName.text = mediaFolderEntity.name
                tvFolderMediaCount.text = mediaFolderEntity.getMediaCount().toString()
                setOnClickListener {
                    clickFolder(mediaFolderEntity)
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}