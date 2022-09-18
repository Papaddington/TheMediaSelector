package com.paddington.dev.preview

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.paddington.dev.bean.MediaEntity
import com.paddington.dev.bean.MediaType

/**
 * <pre>
 *     author : paddington
 *     e-mail : paddington.699@gmail.com
 *     time   : 2020/05/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class PreviewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val previewList: MutableList<MediaEntity> = mutableListOf()
) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return previewList.size
    }

    override fun createFragment(position: Int): Fragment {
        val mediaEntity = previewList[position]
        return when (mediaEntity.type) {
            MediaType.PICTURE -> PreviewPictureFragment.newInstance(mediaEntity)
            MediaType.VIDEO -> PreviewVideoFragment.newInstance(mediaEntity)
        }
    }
}